package com.akrwt.arogya.validation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.akrwt.arogya.activity.MainActivity
import com.akrwt.arogya.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_phone_ver.*

class PhoneVerActivity : AppCompatActivity() {

    private var editTextMobile: EditText? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_ver)

        editTextMobile = findViewById(R.id.editTextMobile)

        buttonContinue.setOnClickListener {

            val mobile = editTextMobile!!.text.toString()

            if (mobile.isEmpty() || mobile.length < 10) {
                editTextMobile!!.error = "Enter a valid mobile number"
            } else {

                val intent = Intent(applicationContext, OtpVerActivity::class.java)
                intent.putExtra("mobile", mobile)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}





