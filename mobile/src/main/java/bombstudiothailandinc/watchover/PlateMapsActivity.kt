package bombstudiothailandinc.watchover

import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class PlateMapsActivity : FragmentActivity() , OnMapReadyCallback {

    private var mMap : GoogleMap? = null
    private var nameString : String? = null
    private var latString : String? = null
    private var lngString : String? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_layout)

        //Receive From Intent
        nameString = intent.getStringExtra("Name")
        latString = intent.getStringExtra("Lat")
        lngString = intent.getStringExtra("Lng")

        //Show view
        val textView = findViewById<View>(R.id.textView10) as TextView
        textView.text = nameString


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    } // main method

    fun clickBackPlate() {
        finish()
    }

    fun clickHistory(view : View) {
        //Start Activity
        startActivity(Intent(this@PlateMapsActivity , HistoryListView::class.java))


    }

    fun onMapReady(googleMap : GoogleMap) {
        mMap = googleMap

        //Create LatLng
        val douLat = java.lang.Double.parseDouble(latString)
        val douLng = java.lang.Double.parseDouble(lngString)
        val latLng = LatLng(douLat , douLng)
        mMap !!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng , 16))
        mMap !!.addMarker(MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.build2))
                .title(nameString))

        //icon and name show onMap

    } // onMap ทำงานควบคุมแผนที่
} // main class