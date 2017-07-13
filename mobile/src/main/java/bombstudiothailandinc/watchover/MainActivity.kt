package bombstudiothailandinc.watchover

import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import com.squareup.okhttp.Call
import com.squareup.okhttp.Callback
import com.squareup.okhttp.FormEncodingBuilder
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.RequestBody
import com.squareup.okhttp.Response

import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity() {

    //Explicit
    private var latTextView : TextView? = null
    private var lngTextView : TextView? = null
    private var plateEditText : EditText? = null

    private var locationManager : LocationManager? = null
    private var criteria : Criteria? = null
    private var GPSABoolean : Boolean = false
    private var networkABoolean : Boolean = false
    private var timeAnInt = 0

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Bind widget
        bindWidget()

        //setup Location
        setupLocation()

        //Auto update
        autoUpdate()

    } //Main method

    fun clickSaveData(view : View) {

        val strName = plateEditText !!.text.toString().trim { it <= ' ' }

        if (strName == "") {
            Toast.makeText(this , "กรุณากรอกชื่อสถานที่ด้วยครับ" ,
                    Toast.LENGTH_SHORT).show()
        } else {
            val strLat = latTextView !!.text.toString()
            val strLng = lngTextView !!.text.toString()
            updateValueToServer(strName , strLat , strLng)
        }

    } //clickSaveData

    private fun updateValueToServer(strName : String , strLat : String , strLng : String) {

        val okHttpClient = OkHttpClient()
        val requestBody = FormEncodingBuilder()
                .add("isAdd" , "true")
                .add("Name" , strName)
                .add("Lat" , strLat)
                .add("Lng" , strLng)
                .build()
        val builder = Request.Builder()
        val request = builder.url("http://androidthai.in.th/dom/addPlate.php").post(requestBody).build()
        val call = okHttpClient.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(request : Request , e : IOException) {

            }

            @Throws(IOException::class)
            override fun onResponse(response : Response) {
                finish()
            }
        })

    }//Update Value

    private fun autoUpdate() {

        timeAnInt += 1


        //Chang Policy
        val threadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(threadPolicy)

        //Get current time
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val date = Date()
        val getTimeDate = dateFormat.format(date)
        Log.d("Test" , "Time ==>$timeAnInt=$getTimeDate")
        myLoop()

    }//Auto update

    private fun myLoop() {
        val handler = Handler()
        handler.postDelayed({ autoUpdate() } , 1000) // minli second
    }

    override fun onResume() {
        super.onResume()

        locationManager !!.removeUpdates(locationListener)

        var strLat = "Unknow"
        var strLng = "Unknow"
        val networkLocation = requestLocation(LocationManager.NETWORK_PROVIDER , "No Internet")
        if (networkLocation != null) {
            strLat = String.format("%.7f" , networkLocation.latitude)
            strLng = String.format("%.7f" , networkLocation.longitude)
        }
        val gpsLocation = requestLocation(LocationManager.GPS_PROVIDER , "No GPS Card")
        if (gpsLocation != null) {
            strLat = String.format("%.7f" , gpsLocation.latitude)
            strLng = String.format("%.7f" , gpsLocation.longitude)
        }
        latTextView !!.text = strLat
        lngTextView !!.text = strLng

    } // onResume

    override fun onStart() {
        super.onStart()
        GPSABoolean = locationManager !!.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (! GPSABoolean) {
            networkABoolean = locationManager !!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (! networkABoolean) {
                Log.d("GPS" , "Cannot Find Location")

            }
        }
    }

    override fun onStop() {
        super.onStop()

        locationManager !!.removeUpdates(locationListener)

    }

    fun requestLocation(strProvider : String , strError : String) : Location? {

        var location : Location? = null
        if (locationManager !!.isProviderEnabled(strProvider)) {


            locationManager !!.requestLocationUpdates(strProvider , 1000 , 10f , locationListener)
            location = locationManager !!.getLastKnownLocation(strProvider)

        } else {
            Log.d("GPS" , strError)
        }


        return location
    }


    val locationListener : LocationListener = object : LocationListener {
        override fun onLocationChanged(location : Location) {
            latTextView !!.text = String.format("%.7f" , location.latitude)
            lngTextView !!.text = String.format("%.7f" , location.longitude)
        }

        override fun onStatusChanged(provider : String , status : Int , extras : Bundle) {

        }

        override fun onProviderEnabled(provider : String) {

        }

        override fun onProviderDisabled(provider : String) {

        }
    }


    private fun setupLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        criteria = Criteria()
        criteria !!.accuracy = Criteria.ACCURACY_FINE
        criteria !!.isAltitudeRequired = false
        criteria !!.isBearingRequired = false
    }

    private fun bindWidget() {
        latTextView = findViewById<View>(R.id.textView3) as TextView
        lngTextView = findViewById<View>(R.id.textView5) as TextView
        plateEditText = findViewById<View>(R.id.editText) as EditText
    }
} //Main Class
