package maps.joaquin.com.googlemapsapi

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.*
import android.view.View

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.widget.Toast
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import android.support.v4.app.ShareCompat.IntentBuilder
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    var latitude:Double? = 0.0
    var longitude:Double? = 0.0
    val PLACE_PICKER_REQUEST = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this, "Current location:\n" + p0, Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        return false
    }

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val searchbutton = findViewById<FloatingActionButton>(R.id.searchbutton)
        searchbutton.setOnClickListener { view ->
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST)
            Snackbar.make(view, "Here's a PlacePicker", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .show()


        }


    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        if (checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            mMap.setOnMyLocationButtonClickListener(this)
            mMap.setOnMyLocationClickListener(this)

        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),1)

        }

    }

    fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        when (parent.id) {
            R.id.sp_tipos -> {
                when (position) {
                    0 -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN;
                        Toast.makeText(this, "MAP TERRAIN", Toast.LENGTH_SHORT).show()


                    }
                    1 -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID;
                        Toast.makeText(this, "MAP HYBRID", Toast.LENGTH_SHORT).show()

                    }
                    3 -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE;
                        Toast.makeText(this, "MAP SATELLITE", Toast.LENGTH_SHORT).show()

                    }
                    4 -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL;
                        Toast.makeText(this, "MAP NORMAL", Toast.LENGTH_SHORT).show()

                    }
                    5 -> {
                        mMap.mapType = GoogleMap.MAP_TYPE_NONE;
                        Toast.makeText(this, "MAP NONE", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                var place:Place = PlacePicker.getPlace(data, this)
                Toast.makeText(this, "Ey", Toast.LENGTH_LONG).show()
                mMap.addMarker(MarkerOptions().position(place.latLng))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
            }
        }
    }

}
