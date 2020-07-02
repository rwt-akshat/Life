package com.akrwt.arogya

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.akrwt.arogya.activity.MainActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    PermissionListener,
    com.google.android.gms.location.LocationListener {
    private var mMap: GoogleMap? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocation: Location? = null
    private var mLocationManager: LocationManager? = null
    private var mLocationRequest: LocationRequest? = null
    private var updateInterval: Long = 2000
    private var fastestInterval: Long = 5000
    private var locationManager: LocationManager? = null
    private var latlng: LatLng? = null
    private var isPermission: Boolean = false


    override fun onConnected(p0: Bundle?) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        startLocationUpdates()

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)

        if (mLocation == null) {
            startLocationUpdates()
        } else {
            Toast.makeText(applicationContext, "Waiting for GPS to locate..", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun startLocationUpdates() {

        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(updateInterval)
            .setFastestInterval(fastestInterval)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }


        LocationServices.FusedLocationApi.requestLocationUpdates(
            mGoogleApiClient,
            mLocationRequest,
            this
        )
    }


    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(p0: Location?) {

        mMap!!.clear()

        latlng = LatLng(p0!!.latitude, p0.longitude)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)


        if (requestSinglePermission()) {
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this)


            mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

            mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


            checkLocation()
        }

    }

    private fun checkLocation(): Boolean {

        if (!isLocationEnabled()) {
            showAlert()
        }
        return isLocationEnabled()
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Enable Location")
            .setMessage("Your Location is off.\nPlease enable location to use this app")
            .setPositiveButton("Location Settings") { _: DialogInterface?, _: Int ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancel") { _: DialogInterface?, _: Int ->
                finish()
            }
            .setCancelable(false)
        builder.create().show()

    }

    private fun isLocationEnabled(): Boolean {

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

    }

    private fun requestSinglePermission(): Boolean {

        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(this)
            .check()

        return isPermission

    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        isPermission = true
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        isPermission = false
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (latlng != null) {
            mMap!!.mapType = GoogleMap.MAP_TYPE_NORMAL
            val bitmapDrawable = resources.getDrawable(R.drawable.ic_health) as BitmapDrawable
            val b = bitmapDrawable.bitmap
            val bmp = Bitmap.createScaledBitmap(b, 100, 100, false)
            mMap!!.addMarker(
                MarkerOptions().position(latlng!!).title("Emergency: Vehicle Impact")
                    .icon(BitmapDescriptorFactory.fromBitmap(bmp))
            )
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16F))

        }
    }

    override fun onStart() {
        super.onStart()

        if (mGoogleApiClient != null) {
            mGoogleApiClient!!.connect()
        }
    }

    override fun onStop() {
        super.onStop()

        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()

    }
}
