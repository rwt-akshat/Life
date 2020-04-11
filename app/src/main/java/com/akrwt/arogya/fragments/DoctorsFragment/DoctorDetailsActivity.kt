package com.akrwt.arogya.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.akrwt.arogya.R
import com.akrwt.arogya.fragments.DoctorsFragment.DoctorUpload
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.fragment_emergency.*
import java.io.ByteArrayOutputStream


class DoctorDetailsActivity : AppCompatActivity() {

    private var TEXT = "text"
    private var SHARED_PREFS = "sharedPrefs"
    var userName: String? = null
    var phn: String? = null
    private var PHONE = "phone"
    private var docName = ""

    private var storage: FirebaseStorage? = null
    private var database: FirebaseDatabase? = null
    private var photo: Uri? = null
    private var cameraRequest = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_emergency)
        supportActionBar!!.hide()

        FirebaseApp.initializeApp(this)

        docName = intent.getStringExtra("name")!!

        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        chooseDoc.setOnClickListener {
            selectImage()
        }

        btnUpload.setOnClickListener {

            when {
                editInjury.text.isEmpty() -> {
                    editInjury.error = "This field cannot be empty"
                }
                etSex.text.isEmpty() -> {
                    etSex.error = "This field cannot be empty"
                }
                editBloodGrp.text.isEmpty() -> {
                    etSex.error = "This field cannot be empty"
                }
                InjuryImage.drawable == null -> {
                    Toast.makeText(this, "Please select a file", Toast.LENGTH_LONG).show()
                }
                else -> {
                    uploadFile(photo!!)
                }
            }
        }
    }

    private fun selectImage() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(i, cameraRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == cameraRequest && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            photo = data.data!!

            Picasso.get()
                .load(photo)
                .into(InjuryImage)
        }
    }


    private fun uploadFile(uri: Uri) {

        val dialog = SpotsDialog.Builder()
            .setContext(this)
            .setTheme(R.style.Custom)
            .build()
            .apply {
                show()
            }

        val storageReference = storage!!.getReference("patients").child(docName)
        val databaseReference = database!!.getReference("patients").child(docName)

        val fileRef = storageReference.child(
            System.currentTimeMillis().toString() +
                    "." + getFileExtension(uri)
        )

        val sharedRef = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        userName = sharedRef!!.getString(TEXT, "DEFAULT")
        phn = sharedRef.getString(PHONE, "DEFAULT")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {

                    val upload =
                        DoctorUpload(
                            userName!!,
                            editInjury.text.toString().trim(),
                            editBloodGrp.text.toString().trim(),
                            etSex.text.toString().trim(),
                            phn!!,
                            it.toString()
                        )

                    val uploadId = databaseReference.push().key
                    databaseReference.child(uploadId!!).setValue(upload)
                    dialog.dismiss()

                    Toast.makeText(
                            this,
                            "Sent Successfully",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "ERROR!!", Toast.LENGTH_LONG).show()
            }
    }

    private fun getFileExtension(uri: Uri): String {
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))!!
    }
}

