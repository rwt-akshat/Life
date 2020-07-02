package com.akrwt.arogya.fragments

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
import android.view.*
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.akrwt.arogya.R
import com.akrwt.arogya.activity.MainActivity
import com.google.android.gms.location.*

class VehicleImpactFragment : Fragment() {

    private lateinit var phoneNumber1: String
    private lateinit var phoneNumber2: String
    private lateinit var phoneNumber3: String
    private lateinit var userName1: String
    private lateinit var userName2: String
    private lateinit var userName3: String

    private lateinit var ambulanceCard:CardView
    private lateinit var towTruckCardView: CardView
    var latitude: Double? = null
    var longitude: Double? = null

    var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_vehicle_impact, container, false)
        setHasOptionsMenu(true)

        ambulanceCard = view.findViewById(R.id.ambulance_card)
        towTruckCardView = view.findViewById(R.id.towCard)


        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())


        val sharedRef = requireContext().getSharedPreferences("FriendNames", Context.MODE_PRIVATE)

        userName1 = sharedRef!!.getString("name1", "DEFAULT")!!
        userName2 = sharedRef.getString("name2", "DEFAULT")!!
        userName3 = sharedRef.getString("name3", "DEFAULT")!!
        phoneNumber1 = sharedRef.getString("phone1", "DEFAULT")!!
        phoneNumber2 = sharedRef.getString("phone2", "DEFAULT")!!
        phoneNumber3 = sharedRef.getString("phone3", "DEFAULT")!!


        towTruckCardView.setOnClickListener {

            val gmmIntentUri = Uri.parse("geo:0,0?q=tow trucks")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        ambulanceCard.setOnClickListener {
            findNavController().navigate(R.id.ambulanceFragment)
        }
        return view
    }


    fun onKeyPress() {

        getLastLocation()

        if (userName1 == "DEFAULT" && userName2 == "DEFAULT" && userName3 == "DEFAULT"
            && phoneNumber1 == "DEFAULT" && phoneNumber2 == "DEFAULT" && phoneNumber3 == "DEFAULT"
        ) {
            Toast.makeText(
                requireContext(),
                "Add friends first...",
                Toast.LENGTH_LONG
            ).show()
        } else {
            if (latitude == null && longitude == null) {
                Toast.makeText(
                    requireContext(),
                    "Try again...didn't get the location..",
                    Toast.LENGTH_LONG
                )
                    .show()
            } else {

                val smsManager = SmsManager.getDefault()
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
                    requireContext(),
                    "Location Sent Successfully to : 1. $userName1\n2. $userName2\n3. $userName3",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.friendsmenu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.addfriends -> findNavController().navigate(R.id.addFriendsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkPermissions(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            MainActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            44
        )
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient!!.lastLocation.addOnCompleteListener { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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