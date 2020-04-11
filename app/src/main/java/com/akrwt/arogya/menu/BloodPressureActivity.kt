package com.akrwt.arogya.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.akrwt.arogya.R
import kotlinx.android.synthetic.main.activity_blood_pressure.*

class BloodPressureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure)

        supportActionBar!!.title = "Blood Pressure"


        getBPBtn.setOnClickListener {

            val intsystolic = eTSystolic.text.toString().toFloat()
            val intdiastolic = eTDiastolic.text.toString().toFloat()


            val builder = AlertDialog.Builder(this)

            builder
                .setTitle("Arogya")
                .setCancelable(true)

            if (intsystolic < 120 && intdiastolic < 80) {

                builder.setMessage("NORMAL")
                val alert = builder.create()
                alert.show()

            }
            if ((intsystolic in 120.0..129.0) && intdiastolic < 80) {

                builder.setMessage("ELEVATED")
                val alert = builder.create()
                alert.show()

            }
            if ((intsystolic in 130.0..139.0) || (intdiastolic in 80.0..89.0)) {

                builder.setMessage("HIGH BLOOD PRESSURE\n(Hypertension)STAGE1")
                val alert = builder.create()
                alert.show()

            }
            if ((intsystolic in 140.0..180.0) || (intdiastolic in 90.0..120.0)) {

                builder.setMessage("HIGH BLOOD PRESSURE\n(Hypertension)STAGE2")
                val alert = builder.create()
                alert.show()

            }
            if (intsystolic > 180 && intdiastolic > 120) {

                builder.setMessage("HYPERTENSIVE CRISES\n(Consult your doctor immediately)")
                val alert = builder.create()
                alert.show()

            }
        }



    }
}
