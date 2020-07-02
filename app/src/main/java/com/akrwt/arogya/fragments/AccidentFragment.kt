package com.akrwt.arogya.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import com.akrwt.arogya.R
import com.akrwt.arogya.utils.AccidentModel
import com.akrwt.docsapp.MySingleton
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class AccidentFragment : Fragment() {

    private var userName: String? = null

    private val fcmApi = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAZu9fBbE:APA91bHH1ejeiLczM49J4PmPI6eM5suCkgiJa3scqSsd0sXVV78zoIxBvmtAv_2qlzlgF425JbYZ4uRXxKf8XgZ2A7cTKNEWDnYxZ3409-PWUjofF5fhGhQsqOfHkP4XTQjPCcm_70dr"

    private var storage: FirebaseStorage? = null
    private var database: FirebaseDatabase? = null
    private var photo: Uri? = null

    private lateinit var editInjuryDetails: EditText
    private lateinit var editAge: EditText
    private lateinit var editContact: EditText
    private lateinit var bloodGrp: String
    private lateinit var chooseDocument: Button
    private lateinit var btnSend: Button
    private lateinit var accidentImage: ImageView
    private lateinit var spinner: Spinner
    private lateinit var radioGroup: RadioGroup
    private lateinit var gender:String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_accident, container, false)

        editInjuryDetails = view.findViewById(R.id.editInjuryDetails)
        editAge = view.findViewById(R.id.editAge)
        editContact = view.findViewById(R.id.editContact)
        chooseDocument = view.findViewById(R.id.chooseDocument)
        btnSend = view.findViewById(R.id.btnSend)
        accidentImage = view.findViewById(R.id.image_view)
        spinner = view.findViewById(R.id.bg_spinner)
        radioGroup = view.findViewById(R.id.radio_grp)
        gender = "Male"

        val bgs = resources.getStringArray(R.array.blood_groups)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, bgs)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                bloodGrp = bgs[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        radioGroup.setOnCheckedChangeListener{group, checked_id->
            val radio:RadioButton = view.findViewById(checked_id)
            gender = radio.text.toString()
        }


        FirebaseApp.initializeApp(requireContext())

        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        val sharedRef = context!!.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        userName = sharedRef!!.getString("text", "DEFAULT")
        val phoneNumber = sharedRef.getString("phone", "DEFAULT")

        editContact.setText(phoneNumber)
        editContact.keyListener = null
        editContact.isEnabled = false


        chooseDocument.setOnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(i, 2)
        }

        btnSend.setOnClickListener {

            when {
                editInjuryDetails.text.isEmpty() -> {
                    editInjuryDetails.error = "This field cannot be empty"
                }
                editAge.text.isEmpty() -> {
                    editAge.error = "This field cannot be empty"
                }
                editContact.text.isEmpty() -> {
                    editContact.error = "This field cannot be empty"

                }
                accidentImage.drawable == null -> {
                    Toast.makeText(context!!, "Please select a file", Toast.LENGTH_LONG).show()
                }
                else -> {
                    uploadFile(photo!!)
                }
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 2 && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            photo = data.data!!

            Picasso.get()
                .load(photo)
                .into(accidentImage)
        }
    }


    private fun uploadFile(uri: Uri) {

        val dialog = SpotsDialog.Builder()
            .setContext(context!!)
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


        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {

                    val upload =
                        AccidentModel(
                            userName!!,
                            editInjuryDetails.text.toString().trim(),
                            gender,
                            editAge.text.toString().trim(),
                            editContact.text.toString().trim(),
                            bloodGrp,
                            it.toString()
                        )

                    val uploadId = databaseReference.push().key
                    databaseReference.child(uploadId!!).setValue(upload)
                    dialog.dismiss()

                    val notification = JSONObject()
                    val notificationBody = JSONObject()
                    try {
                        notificationBody.put("title", "Emergency")
                        notificationBody.put("message", "Emergency Case")
                        notification.put("to", "/topics/Doctor")
                        notification.put("data", notificationBody)

                    } catch (e: JSONException) {
                        Log.e("TAG", "onCreate: " + e.message)
                    }
                    sendNotification(notification)

                    Toast.makeText(
                        context!!,
                        "Sent Successfully",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context!!, "ERROR!!", Toast.LENGTH_LONG).show()
            }
    }

    private fun sendNotification(notification: JSONObject) {

        val jsonObjectRequest = object : JsonObjectRequest(
            Method.POST, fcmApi, notification,
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
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        MySingleton.instance?.addToRequestQueue(jsonObjectRequest, "AccidentActivity")
    }

    private fun getFileExtension(uri: Uri): String {
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(context!!.contentResolver.getType(uri))!!
    }

}