package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.NotificationCompat
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request

import org.json.JSONArray

class FirstActivity : AppCompatActivity() {

    //Explicit
    private var listView : ListView? = null
    private var latStrings : Array<String>? = null
    private var lngStrings : Array<String>? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        listView = findViewById<View>(R.id.listView) as ListView

        //Synchronize JSON
        synJSON()

        //Loop Check User
        loopCheckUser()

    }   // Main Method

    //นี่คือ เมทอด ที่หาระยะ ระหว่างจุด
    private fun distance(lat1 : Double , lon1 : Double , lat2 : Double , lon2 : Double) : Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist *= 60.0 * 1.1515 * 1.609344

        return dist
    }

    private fun deg2rad(deg : Double) : Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad : Double) : Double {
        return rad * 180 / Math.PI
    }

    @SuppressLint("StaticFieldLeak")
    inner class ConnectedLocalUser : AsyncTask<Void , Void , String>() {

        override fun doInBackground(vararg voids : Void) : String? {

            try {

                val okHttpClient = OkHttpClient()
                val builder = Request.Builder()
                val request = builder.url("").build()
                val response = okHttpClient.newCall(request).execute()
                return response.body().string()

            } catch (e : Exception) {
                e.printStackTrace()
                return null
            }

        }   // doInBack

        override fun onPostExecute(s : String) {
            super.onPostExecute(s)
            Log.d("22April" , "JSON ==> " + s)

            try {

                val jsonArray = JSONArray(s)
                val jsonObject = jsonArray.getJSONObject(0)
                val strLat = jsonObject.getString("Lat")
                val strLng = jsonObject.getString("Lng")

                for (i in latStrings !!.indices) {

                    val douLatPlate = java.lang.Double.parseDouble(latStrings !![i])
                    val douLngPlate = java.lang.Double.parseDouble(lngStrings !![i])
                    val douLatUser = java.lang.Double.parseDouble(strLat)
                    val douLngUser = java.lang.Double.parseDouble(strLng)

                    val currentDistance = distance(douLatPlate ,
                            douLngPlate , douLatUser , douLngUser)

                    Log.d("23April" , "current Dis ==> " + currentDistance)

                    if (currentDistance < 0.3) {
                        myNotification() //สามารถเปลี่ยนแปลงได้ถ้าอยากให้ออกตอนร้อง
                    }
                }   // for

            } catch (e : Exception) {
                e.printStackTrace()
            }
        }   // onPost
    }   // Connected Class

    private fun myNotification() {

        Toast.makeText(this , "เข้าในพื้นที่แล้ว" , Toast.LENGTH_SHORT).show()

        val builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.drawable.build2)
        builder.setTicker("Driving Better")
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("เข้าในพื้นที่แล้ว")
        builder.setContentText("เข้าในพื้นที่แล้ว")
        builder.setAutoCancel(false)

        val soundUri = RingtoneManager.getDefaultUri(Notification.DEFAULT_SOUND)
        builder.setSound(soundUri)

        val notification = builder.build()
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1000 , notification)

    }   // myNotification

    private fun loopCheckUser() {

        try {

            val loginStrings = intent.getStringArrayExtra("Login")

            val getLocationWhereIdParent = GetLocationWhereIdParent(this)
            getLocationWhereIdParent.execute(loginStrings[0])

            val strJSON = getLocationWhereIdParent.get()

            val jsonArray = JSONArray(strJSON)
            val jsonObject = jsonArray.getJSONObject(0)
            val strLat = jsonObject.getString("Lat")
            val strLng = jsonObject.getString("Lng")

            for (i in latStrings !!.indices) {

                val douLatPlate = java.lang.Double.parseDouble(latStrings !![i])
                val douLngPlate = java.lang.Double.parseDouble(lngStrings !![i])
                val douLatUser = java.lang.Double.parseDouble(strLat)
                val douLngUser = java.lang.Double.parseDouble(strLng)

                val currentDistance = distance(douLatPlate ,
                        douLngPlate , douLatUser , douLngUser)

                Log.d("23April" , "current Dis ==> " + currentDistance)

                if (currentDistance < 0.3) {
                    myNotification() //สามารถเปลี่ยนแปลงได้ถ้าอยากให้ออกตอนร้อง
                }

            }   // for

        } catch (e : Exception) {
            e.printStackTrace()
        }

        //Delay
        val handler = Handler()
        val intTime = 5000 // หน่วงเป็นเวลา 5 วินาที
        handler.postDelayed({ loopCheckUser() } , intTime.toLong())

    }   // loopCheckUser

    override fun onRestart() {
        super.onRestart()
        synJSON()
    }

    private fun synJSON() {
        val mySynJSON = MySynJSON()
        mySynJSON.execute()
    }

    //Create Inner Class
    @SuppressLint("StaticFieldLeak")
    inner class MySynJSON : AsyncTask<Void , Void , String>() {

        override fun doInBackground(vararg voids : Void) : String? {

            try {

                val okHttpClient = OkHttpClient()
                val builder = Request.Builder()
                val request = builder.url("").build()
                val response = okHttpClient.newCall(request).execute()
                return response.body().string()

            } catch (e : Exception) {
                e.printStackTrace()
                return null
            }
        }   // doInBack

        override fun onPostExecute(s : String) {
            super.onPostExecute(s)

            Log.d("21April" , "JSON ==> " + s)

            try {

                val jsonArray = JSONArray(s)

                val nameStrings = arrayOfNulls<String>(jsonArray.length())
                //latStrings = arrayOfNulls(jsonArray.length())
                //lngStrings = arrayOfNulls(jsonArray.length())

                for (i in 0 until jsonArray.length()) {

                    val jsonObject = jsonArray.getJSONObject(i)
                    nameStrings[i] = jsonObject.getString("Name")
                    //latStrings[i] = jsonObject.getString("Lat")
                    //lngStrings[i] = jsonObject.getString("Lng")

                    Log.d("21April" , "Name ==> " + i + " == " + nameStrings[i])

                }   // for

                Log.d("21April" , "Name length ==> " + nameStrings.size)

                val plateAdapter = PlateAdapter(this@FirstActivity , nameStrings)
                listView !!.adapter = plateAdapter

                listView !!.onItemClickListener = AdapterView.OnItemClickListener { _ , _ , i , _ ->
                    val intent = Intent(this@FirstActivity , PlateMapsActivity::class.java)

                    intent.putExtra("Name" , nameStrings[i])
                    intent.putExtra("Lat" , latStrings !![i])
                    intent.putExtra("Lng" , lngStrings !![i])

                    startActivity(intent)
                }   // onItem

            } catch (e : Exception) {
                e.printStackTrace()
            }
        }   // onPost
    }   // MySynJSON class

    fun clickAddPlate() {
        startActivity(Intent(this@FirstActivity , MainActivity::class.java))
    }

}   // Main Class