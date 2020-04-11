package com.akrwt.arogya.validation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import com.akrwt.arogya.MainActivity
import com.akrwt.arogya.R
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user_info.*
import java.io.ByteArrayOutputStream

class UserInfoActivity : AppCompatActivity() {


    private var pickImageRequest = 1
    private var cameraRequest = 2
    private var mImageUri: Uri? = null
    private var SHARED_PREFS = "sharedPrefs"
    private var TEXT = "text"
    private var PHONE = "phone"
    private var USER_IMAGE = "userImage"
    private var sharedPreferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)

        chooseImageBtn.setOnClickListener {

            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(i, pickImageRequest)
        }

        CameraBtn.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequest)
        }

        savingDetails()
    }


    private fun savingDetails() {


        val phoneNumber = FirebaseAuth.getInstance().currentUser!!.phoneNumber
        editTextNumber.setText(phoneNumber)
        editTextNumber.keyListener = null
        editTextNumber.isEnabled = false


        buttonGo.setOnClickListener {
            when {
                UserImageView.drawable == null -> {
                    Toast.makeText(applicationContext, "No file selected", Toast.LENGTH_SHORT)
                        .show()
                }
                editTextFullName.text.toString().trim().isEmpty() -> {

                    editTextFullName.error = "This field cannot be empty.."

                }
                else -> {

                    sharedPreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
                    val editor = sharedPreferences!!.edit()
                    editor.putString(TEXT, editTextFullName.text.toString())
                    editor.putString(PHONE, editTextNumber.text.toString())
                    val img = bitmapToString(UserImageView.drawable.toBitmap(200, 200))
                    editor.putString(USER_IMAGE, img)
                    editor.apply()

                    Toast.makeText(applicationContext, "Details Saved", Toast.LENGTH_LONG)
                        .show()

                    startActivity(Intent(this, MainActivity::class.java))

                }
            }
        }
    }

    private fun bitmapToString(bitmap: Bitmap): String {

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val arr = baos.toByteArray()
        return Base64.encodeToString(arr, Base64.DEFAULT)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            mImageUri = data.data!!

            Picasso.get()
                .load(mImageUri)
                .into(UserImageView)
        }

        if (requestCode == cameraRequest && resultCode == Activity.RESULT_OK) {
            val photo = data!!.extras!!.get("data") as Bitmap
            UserImageView.setImageBitmap(photo)
        }
    }

}
