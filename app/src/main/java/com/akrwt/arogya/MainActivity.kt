package com.akrwt.arogya

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import com.akrwt.arogya.menu.BMIActivity
import com.akrwt.arogya.menu.BloodPressureActivity
import com.akrwt.arogya.menu.FatActivity
import com.akrwt.arogya.fragments.*
import com.akrwt.arogya.fragments.DashboardFragment.Dashboard.DashBoardFragment
import com.akrwt.arogya.validation.PhoneVerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.Exception


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var TEXT = "text"
    private var USER_IMAGE = "userImage"
    private var SHARED_PREFS = "sharedPrefs"
    var userName: String? = null
    var img: String? = null
    var phn:String?=null
    private var PHONE = "phone"


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_dashboard -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    DashBoardFragment()
                ).commit()
            }
            R.id.nav_inj->
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    DoctorsFragment()
                ).commit()
            R.id.nav_bloodreq ->
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    BloodReqFragment()
                ).commit()

            R.id.nav_maps ->
                startActivity(Intent(this,MapsActivity::class.java))

            R.id.feedbck -> startActivity(Intent(this,FeedbackActivity::class.java))

            R.id.nav_share ->
                try {
                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Arogya")
                    var shareMessage= "\nLet me recommend you this application\n\n"
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "choose one"))
                } catch(ex:Exception) {
                    ex.toString()
                }


            R.id.nav_sign_out -> {
                Toast.makeText(applicationContext, "Signed Out", Toast.LENGTH_LONG).show()
                FirebaseAuth.getInstance().signOut()
                intent = Intent(applicationContext, PhoneVerActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
        drawer!!.closeDrawer(GravityCompat.START)
        return true
    }

    private var drawer: DrawerLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        FirebaseMessaging.getInstance().subscribeToTopic("all")

        toolbar.overflowIcon!!.setColorFilter(
            ContextCompat.getColor(this,R.color.black),
            PorterDuff.Mode.SRC_ATOP)
        setSupportActionBar(toolbar)



        val sharedRef = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        userName = sharedRef!!.getString(TEXT, "DEFAULT")
        img = sharedRef.getString(USER_IMAGE, "DEFAULT")
        phn=sharedRef.getString(PHONE,"DEFAULT")


        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer!!.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                DashBoardFragment()
            ).commit()

            navigationView.setCheckedItem(R.id.nav_dashboard)
        }

            var headerView = navigationView.getHeaderView(0)
            var usrName = headerView.findViewById<TextView>(R.id.NameIni)
            var phSub = headerView.findViewById<TextView>(R.id.PhoneNum)

        usrName.text = userName
        phSub.text = phn



    }

    override fun onBackPressed() {
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setCheckedItem(R.id.nav_dashboard)

        if (drawer!!.isDrawerOpen(GravityCompat.START)) {
            drawer!!.closeDrawer(GravityCompat.START)

        } else {

            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                DashBoardFragment()
            ).commit()

            val frags = supportFragmentManager.fragments
            if (frags != null) {
                for (fragment in frags) {
                    if (fragment != null && fragment.isVisible)
                        if(fragment.toString().substring(0,17) == "DashBoardFragment"){
                            finish()
                        }
                }

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.overflow_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.bloodpressure->startActivity(Intent(this,BloodPressureActivity::class.java))
            R.id.bmi->startActivity(Intent(this,BMIActivity::class.java))
            R.id.fat->startActivity(Intent(this,FatActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}


