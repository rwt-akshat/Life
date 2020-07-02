package com.akrwt.arogya.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.akrwt.arogya.R
import com.akrwt.arogya.validation.PhoneVerActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Timer().schedule(object : TimerTask() {
            override fun run() {
                if (currentUser == null) {
                    val intent = Intent(this@SplashActivity, PhoneVerActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }, 1000L)
    }

}
