package com.akrwt.arogya.fragments.DashboardFragment.Impact

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.akrwt.arogya.R
import com.akrwt.docsapp.MySingleton
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_accident.*
import kotlinx.android.synthetic.main.fragment_emergency.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class AccidentActivity : AppCompatActivity() {

    private var TEXT = "text"
    private var SHARED_PREFS = "sharedPrefs"
    var userName: String? = null
    var phn: String? = null
    private var PHONE = "phone"

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAZu9fBbE:APA91bHH1ejeiLczM49J4PmPI6eM5suCkgiJa3scqSsd0sXVV78zoIxBvmtAv_2qlzlgF425JbYZ4uRXxKf8XgZ2A7cTKNEWDnYxZ3409-PWUjofF5fhGhQsqOfHkP4XTQjPCcm_70dr"
    private val contentType = "application/json"

    private var storage: FirebaseStorage? = null
    private var database: FirebaseDatabase? = null
    private var photo: Uri? = null
    private var cameraRequest = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accident)

        FirebaseApp.initializeApp(this)

        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        val sharedRef = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        userName = sharedRef!!.getString(TEXT, "DEFAULT")
        phn = sharedRef.getString(PHONE, "DEFAULT")

        editContact.setText(phn)
        editContact.keyListener = null
        editContact.isEnabled = false


        chseDoc.setOnClickListener {
            selectImage()
        }

        btnSend.setOnClickListener {

            when {
                editInjuryDetails.text.isEmpty() -> {
                    editInjury.error = "This field cannot be empty"
                }
                editSex.text.isEmpty() -> {
                    editSex.error = "This field cannot be empty"
                }
                editAge.text.isEmpty() -> {
                    editAge.error = "This field cannot be empty"
                }
                editContact.text.isEmpty()->{
                    editContact.error="This field cannot be empty"

                }
                AccidentImage.drawable == null -> {
                    Toast.makeText(this, "Please select a file", Toast.LENGTH_LONG).show()
                }
                else -> {
                    uploadFile(photo!!)
                }
            }
        }

    }

    fun selectImage() {
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
                .into(AccidentImage)
        }
    }


    private fun uploadFile(uri: Uri) {

        var mUploadTask: StorageTask<UploadTask.TaskSnapshot>? = null

        val dialog = SpotsDialog.Builder()
            .setContext(this)
            .setTheme(R.style.Custom)
            .build()
            .apply {
                show()
            }

        val storageReference = storage!!.getReference("accident")
        val databaseReference = database!!.getReference("accident")

        val fileRef = storageReference.child(
            System.currentTimeMillis().toString() +
                    "." + getFileExtension(uri)
        )




        mUploadTask = fileRef.putFile(uri)

            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {

                    val upload =
                        Accident(
                            userName!!,
                            editInjuryDetails.text.toString().trim(),
                            editSex.text.toString().trim(),
                            editAge.text.toString().trim(),
                            editContact.text.toString().trim(),
                            editBG.text.toString().trim(),
                            it.toString()
                        )

                    val uploadId = databaseReference.push().key
                    databaseReference.child(uploadId!!).setValue(upload)
                    dialog.dismiss()




                    val notification = JSONObject()
                    val notificationBody = JSONObject()
                    try {
                        notificationBody.put("title", "Emergency")
                        notificationBody.put("message","Emergency Case")
                        notification.put("to", "/topics/Doctor")
                        notification.put("data", notificationBody)

                    } catch (e: JSONException) {
                        Log.e("TAG", "onCreate: " + e.message)
                    }
                    sendNotification(notification)

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
    private fun sendNotification(notification: JSONObject) {

        val tag = AccidentActivity::class.java.simpleName

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, FCM_API, notification,
            Response.Listener { response ->
                try {
                    Log.i("Response", "onResponse: $response")

                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            ,
            Response.ErrorListener { error ->
                error.printStackTrace()
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = serverKey
                headers["Content-Type"] = contentType
                return headers
            }
        }
        MySingleton.instance?.addToRequestQueue(jsonObjectRequest, tag)

    }

    private fun getFileExtension(uri: Uri): String {
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))!!
    }
}


