package com.akrwt.arogya.fragments.DashboardFragment.Impact

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.telephony.SmsManager
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.akrwt.arogya.fragments.DashboardFragment.Ambulance.AmbulanceActivity
import com.akrwt.arogya.R
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_impact.*


class ImpactActivity : AppCompatActivity() {
    var PERMISSION_ID = 44

    private var SHARED_PREFS = "FriendNames"
    private var NAME1 = "name1"
    private var NAME2 = "name1"
    private var NAME3 = "name1"
    private var PHONE1 = "phone1"
    private var PHONE2 = "phone2"
    private var PHONE3 = "phone3"
    private var phoneNumber1: String? = null
    private var phoneNumber2: String? = null
    private var phoneNumber3: String? = null
    var userName1: String? = null
    var userName2: String? = null
    var userName3: String? = null
    var latitude: Double? = null
    var longitude: Double? = null


    var mFusedLocationClient: FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_impact)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.SEND_SMS),
                    123
                )
            }
        }


        val sharedRef = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        userName1 = sharedRef!!.getString(NAME1, "DEFAULT")
        userName2 = sharedRef.getString(NAME2, "DEFAULT")
        userName3 = sharedRef.getString(NAME3, "DEFAULT")
        phoneNumber1 = sharedRef.getString(PHONE1, "DEFAULT")
        phoneNumber2 = sharedRef.getString(PHONE2, "DEFAULT")
        phoneNumber3 = sharedRef.getString(PHONE3, "DEFAULT")



        btntowTrucks.setOnClickListener {

            var gmmIntentUri = Uri.parse("geo:0,0?q=tow trucks")
            var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        btnAmbulance.setOnClickListener{
                startActivity(Intent(this,
                    AmbulanceActivity::class.java))
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {

        val action=event!!.action
        val keyCode=event.keyCode

        when(keyCode){
            KeyEvent.KEYCODE_VOLUME_UP->{
                if(action== KeyEvent.ACTION_DOWN) {

                    getLastLocation()

                    if (userName1 == "DEFAULT" && userName2 == "DEFAULT" && userName3 == "DEFAULT"
                        && phoneNumber1 == "DEFAULT" && phoneNumber2 == "DEFAULT" && phoneNumber3 == "DEFAULT"
                    ) {
                        Toast.makeText(
                            applicationContext,
                            "Add friends first...",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        if (latitude == null && longitude == null) {
                            Toast.makeText(
                                applicationContext,
                                "Try again...didn't get the location..",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        } else {

                            var smsManager = SmsManager.getDefault()
                            smsManager.sendTextMessage(
                                phoneNumber1,
                                null,
                                "Hey there! Need your help.\nhttps://maps.google.com/maps?q=loc:$latitude,$longitude",
                                null,
                                null
                            )
                            smsManager.sendTextMessage(
                                phoneNumber2,
                                null,
                                "Hey there! Need your help.\nhttps://maps.google.com/maps?q=loc:$latitude,$longitude",
                                null,
                                null
                            )
                            smsManager.sendTextMessage(
                                phoneNumber3,
                                null,
                                "Hey there! Need your help.\nhttps://maps.google.com/maps?q=loc:$latitude,$longitude",
                                null,
                                null
                            )

                            Toast.makeText(
                                applicationContext,
                                "Location Sent Successfully to : 1. $userName1\n2. $userName2\n3. $userName3",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }
                }
                return true
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.friendsmenu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.addfriends -> startActivity(Intent(this, AddFriendsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Granted. Start getting the location information
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener(
                    object : OnCompleteListener<Location?> {
                        override fun onComplete(task: Task<Location?>) {
                            val location: Location? = task.result
                            if (location == null) {
                                requestNewLocationData()
                            } else {
                                latitude = location.latitude
                                longitude = location.longitude
                            }
                        }
                    }
                )
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude
        }
    }


}