package com.akrwt.arogya.fragments.DashboardFragment.Impact

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.akrwt.arogya.R
import kotlinx.android.synthetic.main.activity_add_friends.*

class AddFriendsActivity : AppCompatActivity() {

    private var SHARED_PREFS = "FriendNames"
    private var NAME1 = "name1"
    private var NAME2 = "name1"
    private var NAME3 = "name1"
    private var PHONE1 = "phone1"
    private var PHONE2 = "phone2"
    private var PHONE3 = "phone3"

    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)


        btnSave.setOnClickListener {

            when {
                etName1.text.toString().trim().isEmpty() -> {
                    etName1.error = "This field cannot be empty.."
                }
                etName2.text.toString().trim().isEmpty() -> {
                    etName2.error = "This field cannot be empty.."
                }
                etName3.text.toString().trim().isEmpty() -> {
                    etName3.error = "This field cannot be empty.."
                }
                etPhone1.text.toString().trim().isEmpty() -> {
                    etPhone1.error = "This field cannot be empty.."
                }
                etPhone2.text.toString().trim().isEmpty() -> {
                    etPhone2.error = "This field cannot be empty.."
                }
                etPhone3.text.toString().trim().isEmpty() -> {
                    etPhone3.error = "This field cannot be empty.."
                }
                else -> {

                    sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                    val editor = sharedPreferences!!.edit()
                    editor.putString(NAME1, etName1.text.toString())
                    editor.putString(NAME2, etName2.text.toString())
                    editor.putString(NAME3, etName3.text.toString())
                    editor.putString(PHONE1, etPhone1.text.toString())
                    editor.putString(PHONE2, etPhone2.text.toString())
                    editor.putString(PHONE3, etPhone3.text.toString())
                    editor.apply()

                    Toast.makeText(applicationContext, "Details Saved", Toast.LENGTH_LONG)
                        .show()

                    startActivity(Intent(this, ImpactActivity::class.java))

                }
            }

        }


    }
}
