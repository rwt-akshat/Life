package com.akrwt.arogya.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.akrwt.arogya.R
import kotlinx.android.synthetic.main.activity_bmi.*

class BMIActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)
        supportActionBar!!.title = "BMI Calculator"


        getBtn.setOnClickListener {
            val height = editTextHeight.text.toString().toFloat()
            val weight = editTextWeight.text.toString().toFloat()

            val cal: Float = weight / (height * height)

            val builder = AlertDialog.Builder(this)

            builder
                .setTitle("BMI CALCULATOR")
                .setCancelable(true)

            if (cal <= 18.50) {
                builder.setMessage("BMI :: $cal\nUNDERWEIGHT")
                val alert = builder.create()
                alert.show()
            }

            if (cal > 18.50 && cal <= 23.0) {
                builder.setMessage("BMI :: $cal\nHEALTHY")
                val alert = builder.create()
                alert.show()
            }

            if (cal > 23.0 && cal <= 27.50) {
                builder.setMessage("BMI :: $cal\nOVERWEIGHT")
                val alert = builder.create()
                alert.show()
            }

            if (cal > 27.50) {
                builder.setMessage("BMI :: $cal\nOBESE")
                val alert = builder.create()
                alert.show()
            }
        }
    }
}
