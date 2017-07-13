package bombstudiothailandinc.watchover

import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView

import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response

import org.json.JSONArray
import org.json.JSONObject

import java.util.ArrayList
import java.util.Objects

class HistoryListView : AppCompatActivity() {

    private var listView : ListView? = null
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_list_view)

        listView = findViewById<View>(R.id.listView2)
        val connectedHistory = ConnectedHistory()
        connectedHistory.execute()
    } //Main Method

    inner class ConnectedHistory : AsyncTask<Void , Void , String>() {

        override fun doInBackground(vararg params : Void) : String? {

            try {

                val okHttpClient = OkHttpClient()
                val builder = Request.Builder()
                val request = builder.url("http://swiftcodingthai.com/watch/php_get_history.php").build()
                val response = okHttpClient.newCall(request).execute()
                return response.body().string()
            } catch (e : Exception) {
                e.printStackTrace()
                return null
            }

        } //doInBack

        override fun onPostExecute(s : String) {
            super.onPostExecute(s)

            try {

                val jsonArray = JSONArray(s)

                val allTimeStrings = arrayOfNulls<String>(jsonArray.length())
                val allDateStrings = arrayOfNulls<String>(jsonArray.length())

                val stringArrayList = ArrayList<String>()

                for (i in 0 .. jsonArray.length() - 1) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    allTimeStrings[i] = jsonObject.getString("Date")
                    val resultStrings = allTimeStrings[i].split("".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    allDateStrings[i] = resultStrings[0]
                    stringArrayList.add(allDateStrings[i])

                    Log.d("24April" , "Date(" + i + ") =" + allDateStrings[i])

                } //for

                val objects = stringArrayList.toTypedArray() as Array<Objects>
                for (objects1 in objects) {
                    if (stringArrayList.indexOf(objects1) != stringArrayList.lastIndexOf(objects1)) {
                        stringArrayList.removeAt(stringArrayList.lastIndexOf(objects1))

                    }//if
                }// for

                val stringArrayAdapter = ArrayAdapter(this@HistoryListView ,
                        android.R.layout.simple_list_item_1 , stringArrayList)
                listView !!.adapter = stringArrayAdapter

            } catch (e : Exception) {
                e.printStackTrace()
            }

        } //onPost
    } //connectedClass
} //Main Class
