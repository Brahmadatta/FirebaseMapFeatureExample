package com.example.firebasemapfeatureexample

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapActivity : AppCompatActivity(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var map: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val TIME_TO_CHECK_LOCATION = 10 * 1000L
        // 3
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationCallback = object : LocationCallback(){
            override fun onLocationResult(locationResult : LocationResult) {
                lastLocation = locationResult.lastLocation
//                val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
//                intent.putExtra(EXTRA_LOCATION, lastLocation)
//                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
//
//                // Updates notification content if this service is running as a foreground
//                // service.
//                if (serviceRunningInForeground) {
//                    notificationManager.notify(
//                        NOTIFICATION_ID,
//                        generateNotification(currentLocation))
//                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest = LocationRequest()
        locationRequest = LocationRequest.create().apply {
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = TIME_TO_CHECK_LOCATION
            locationRequest.fastestInterval = TIME_TO_CHECK_LOCATION
            locationRequest.maxWaitTime = TIME_TO_CHECK_LOCATION
        }

        checkPermissionMap()

    }

    private fun checkPermissionMap() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            askForLocationPermissions();
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    lastLocation = location
                    Log.e("locationData",location.latitude.toString() + " " + location.longitude.toString())
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    placeMarkerOnMap(currentLatLng)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,
                        Looper.getMainLooper()
                    )
                }
            }
        }
    }

    fun checkPermission() : Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                AlertDialog.Builder(this)
                    .setTitle("Location Permission")
                    .setMessage("Location Permission")
                    .setPositiveButton(
                        android.R.string.ok,
                        DialogInterface.OnClickListener { dialogInterface, i -> //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(
                                this,
                                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                LOCATION_PERMISSION_REQUEST_CODE
                            )
                        })
                    .create()
                    .show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )
            }
            return false
        } else {
            return true
        }

    }

    private fun askForLocationPermissions() {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            AlertDialog.Builder(this)
                .setTitle("Location permessions needed")
                .setMessage("you need to allow this permission!")
                .setPositiveButton("Sure", DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                })
                .setNegativeButton("Not now",
                    DialogInterface.OnClickListener { dialog, which ->
                        //                                        //Do nothing
                    })
                .show()

            // Show an expanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        } else {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )

            // MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    fun isPermissionGranted(
        grantPermissions: Array<String?>, grantResults: IntArray,
        permission: String
    ): Boolean {
        for (i in grantPermissions.indices) {
            if (permission == grantPermissions[i]) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker) = false


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                Log.e("locationDatasetUpMap",location.latitude.toString() + " " + location.longitude.toString())

            }
            fusedLocationClient.requestLocationUpdates(locationRequest,locationCallback,
                Looper.getMainLooper()
            )
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> if (isPermissionGranted(
                    permissions,
                    grantResults,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                //Do you work
                setUpMap()

            } else {
                Toast.makeText(this, "Can not proceed! i need permission", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        val markerOptions = MarkerOptions().position(location)
        // 2
        map.addMarker(markerOptions)
    }

    override fun onResume() {
        super.onResume()
        checkPermissionMap()
//        if (checkPermission()){
//            if (ContextCompat.checkSelfPermission(this,
//                    Manifest.permission. ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//                // do your stuff here
//                onMapReady(map)
//            }
//        }
    }

}