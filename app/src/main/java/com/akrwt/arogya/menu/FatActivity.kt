package com.akrwt.arogya.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.akrwt.arogya.R
import kotlinx.android.synthetic.main.activity_fat.*

class FatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fat)
        supportActionBar!!.title = "Fat Percentage"

        getFBtn.setOnClickListener {

            var age = editFAge.text.toString().toFloat()
            var weight = editFWeight.text.toString().toFloat()
            var height = editFHeight.text.toString().toFloat()


            var fat = (977.17 * weight) / (height * height) + (0.16 * age) - 19.34


            val builder = AlertDialog.Builder(this)

            builder
                .setTitle("FAT PERCENTAGE")
                .setCancelable(true)

            builder.setMessage("$fat%")
            val alert = builder.create()
            alert.show()
        }

    }
}
