package com.akrwt.arogya.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.KeyEvent
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.akrwt.arogya.BuildConfig
import com.akrwt.arogya.MapsActivity
import com.akrwt.arogya.R
import com.akrwt.arogya.validation.PhoneVerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        checkPermission()

        val navController = Navigation.findNavController(
            this,
            R.id.fragment_container
        )
        setupActionBarWithNavController(navController, drawer_layout)
        nav_view.setupWithNavController(navController)

        FirebaseMessaging.getInstance().subscribeToTopic("all")
        val sharedRef = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val userName = sharedRef!!.getString("text", "DEFAULT")
        //val img = sharedRef.getString("userImage", "DEFAULT")
        val phn = sharedRef.getString("phone", "DEFAULT")

        val headerView = nav_view.getHeaderView(0)
        val usrName: TextView = headerView.findViewById(R.id.NameIni)
        val phSub: TextView = headerView.findViewById(R.id.PhoneNum)

        usrName.text = userName
        phSub.text = phn

        nav_view.menu.findItem(R.id.nav_share).setOnMenuItemClickListener {
            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Arogya")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage =
                    shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (ex: Exception) {
                Log.e("Share Exception", ex.toString())
            }
            return@setOnMenuItemClickListener true
        }
        nav_view.menu.findItem(R.id.nav_sign_out).setOnMenuItemClickListener {
            Toast.makeText(applicationContext, "Signed Out", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            intent = Intent(applicationContext, PhoneVerActivity::class.java)
            startActivity(intent)
            finish()
            return@setOnMenuItemClickListener true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(
            this,
            R.id.fragment_container
        )
        return NavigationUI.navigateUp(navController, drawer_layout)
    }

    /* override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {

         if (keyCode == KeyEvent.ACTION_UP) {
             Toast.makeText(applicationContext, "HHEHEHEHE", Toast.LENGTH_LONG).show()
         }

         return super.onKeyUp(keyCode, event)
     }*/

    private fun checkPermission() {
        Dexter.withContext(this).withPermissions(
            Manifest.permission.SEND_SMS,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (!report!!.areAllPermissionsGranted()) {
                    finishAffinity()
                }
                if (report.isAnyPermissionPermanentlyDenied) {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: MutableList<PermissionRequest>?,
                p1: PermissionToken?
            ) {
                p1!!.continuePermissionRequest()
            }
        }).onSameThread().check()
    }
}



