package com.akrwt.arogya.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.akrwt.arogya.R
import com.akrwt.arogya.utils.DoctorDetailsUploadModel
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import dmax.dialog.SpotsDialog

class AskDoctorDetailsFragment : Fragment() {

    private var userName: String? = null
    private var phoneNumber: String? = null
    private var docName = ""
    private var storage: FirebaseStorage? = null
    private var database: FirebaseDatabase? = null
    private var photo: Uri? = null
    private var cameraRequest = 2

    private lateinit var editInjury: EditText
    private lateinit var etGender: EditText
    private lateinit var editBloodGrp: EditText
    private lateinit var chooseImage: Button
    private lateinit var injuryImage: ImageView
    private lateinit var btnUpload: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ask_doctor_details, container, false)
        editBloodGrp = view.findViewById(R.id.editBloodGrp)
        etGender = view.findViewById(R.id.etGender)
        editInjury = view.findViewById(R.id.editInjury)
        chooseImage = view.findViewById(R.id.chooseImage)
        injuryImage = view.findViewById(R.id.InjuryImage)
        btnUpload = view.findViewById(R.id.btnUpload)



        FirebaseApp.initializeApp(requireContext())
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()

        chooseImage.setOnClickListener {
            val i = Intent()
            i.type = "image/*"
            i.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(i, cameraRequest)
        }

        btnUpload.setOnClickListener {

            when {
                editInjury.text.isEmpty() -> {
                    editInjury.error = "This field cannot be empty"
                }
                etGender.text.isEmpty() -> {
                    etGender.error = "This field cannot be empty"
                }
                editBloodGrp.text.isEmpty() -> {
                    editBloodGrp.error = "This field cannot be empty"
                }
                injuryImage.drawable == null -> {
                    Toast.makeText(requireContext(), "Please select a file", Toast.LENGTH_LONG)
                        .show()
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

        if (requestCode == cameraRequest && resultCode == Activity.RESULT_OK
            && data != null && data.data != null
        ) {
            photo = data.data!!

            Picasso.get()
                .load(photo)
                .into(injuryImage)
        }
    }


    private fun uploadFile(uri: Uri) {

        val dialog = SpotsDialog.Builder()
            .setContext(requireContext())
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

        val sharedRef = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        userName = sharedRef!!.getString("text", "DEFAULT")
        phoneNumber = sharedRef.getString("phone", "DEFAULT")

        fileRef.putFile(uri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener {

                    val upload =
                        DoctorDetailsUploadModel(
                            userName!!,
                            editInjury.text.toString().trim(),
                            editBloodGrp.text.toString().trim(),
                            etGender.text.toString().trim(),
                            phoneNumber!!,
                            it.toString()
                        )

                    val uploadId = databaseReference.push().key
                    databaseReference.child(uploadId!!).setValue(upload)
                    dialog.dismiss()

                    Toast.makeText(
                        requireContext(),
                        "Sent Successfully",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun getFileExtension(uri: Uri): String {
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(requireContext().contentResolver.getType(uri))!!
    }
}