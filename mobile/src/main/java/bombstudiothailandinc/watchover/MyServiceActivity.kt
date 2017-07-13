package bombstudiothailandinc.watchover

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import org.json.JSONArray
import org.json.JSONObject

@SuppressLint("Registered")
class MyServiceActivity : FragmentActivity() , OnMapReadyCallback {

    private var mMap : GoogleMap? = null
    private var loginString : Array<String>? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.service_layout)

        //get Value From Inten
        getValueFromInten()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        addMapFragment()


        //Back
        backController()

        //Save
        saveController()

        //List
        listController()


    } //Main Method

    private fun listController() {
        val imageView = findViewById<View>(R.id.imvListView) as ImageView
        imageView.setOnClickListener {
            val intent = Intent(this@MyServiceActivity , FirstActivity::class.java)
            intent.putExtra("Login" , loginString)
            startActivity(intent)
        }
    }

    private fun saveController() {
        val imageView = findViewById<View>(R.id.imvSave) as ImageView
        imageView.setOnClickListener {
            val intent = Intent(this@MyServiceActivity , AddChildActivity::class.java)
            intent.putExtra("Login" , loginString)
            startActivity(intent)
        }
    }

    private fun backController() {
        val imageView = findViewById<View>(R.id.imvBack) as ImageView
        imageView.setOnClickListener { finish() }


    }

    private fun getValueFromInten() {

        loginString = intent.getStringArrayExtra("Login")
    }

    private fun addMapFragment() {
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    fun onMapReady(googleMap : GoogleMap) {
        mMap = googleMap
        val tag = "13JulyV1"

        try {

            Log.d(tag , "id_Parent ==> " + loginString !![0])

            val getLocationWhereIdParent = GetLocationWhereIdParent(this)
            getLocationWhereIdParent.execute(loginString !![0])

            val strJSON = getLocationWhereIdParent.get()
            Log.d(tag , "JSON ==> " + strJSON)

            val jsonArray = JSONArray(getLocationWhereIdParent.get())
            val jsonObject = jsonArray.getJSONObject(0)

            val strLat = jsonObject.getString("Lat")
            val strLng = jsonObject.getString("Lng")
            Log.d(tag , "Lat ==> " + strLat)
            Log.d(tag , "Lng ==> " + strLng)

            val latLng = LatLng(java.lang.Double.parseDouble(strLat) , java.lang.Double.parseDouble(strLng))

            mMap !!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 15))

            val markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            mMap !!.addMarker(markerOptions)


        } catch (e : Exception) {
            Log.d(tag , "e ==> " + e.toString())
        }

    } //omMapReady

} //Main Class