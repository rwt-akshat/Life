package com.akrwt.arogya.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.akrwt.arogya.activity.MainActivity
import com.akrwt.arogya.R
import com.akrwt.docsapp.MySingleton
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class BloodReqFragment : Fragment() {

    private val fcmApi = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAZu9fBbE:APA91bHH1ejeiLczM49J4PmPI6eM5suCkgiJa3scqSsd0sXVV78zoIxBvmtAv_2qlzlgF425JbYZ4uRXxKf8XgZ2A7cTKNEWDnYxZ3409-PWUjofF5fhGhQsqOfHkP4XTQjPCcm_70dr"

    private lateinit var spinner: Spinner
    private lateinit var bloodGrp: String
    private lateinit var bgName: EditText
    private lateinit var bgAge: EditText
    private lateinit var bgPhone: EditText
    private lateinit var bgDes: EditText
    private lateinit var bgSend: Button

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bloodgrp, container, false)

        bgName = view.findViewById(R.id.BGName)
        bgAge = view.findViewById(R.id.BGAge)
        bgPhone = view.findViewById(R.id.BGPhone)
        bgDes = view.findViewById(R.id.BGDes)
        bgSend = view.findViewById(R.id.btnSend)
        spinner = view.findViewById(R.id.bg_spinner)


        val sharedRef = requireContext().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val bgs = resources.getStringArray(R.array.blood_groups)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,bgs)
        spinner.adapter = adapter

        spinner.onItemSelectedListener =object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bloodGrp = bgs[position]
            }

        }

        val phn = sharedRef.getString("phone", "DEFAULT")

        bgPhone.setText(phn)
        bgPhone.keyListener = null
        bgPhone.isEnabled = false

        sendDetails()
        return view
    }

    private fun sendDetails() {

         bgSend.setOnClickListener {

             when {
                 bgAge.text.toString() == "" -> {
                     bgAge.error = "This field is empty"
                     bgAge.requestFocus()
                 }
                 bgName.text.toString() == "" -> {
                     bgName.error = "This field is empty"
                     bgName.requestFocus()
                 }
                 bgPhone.text.toString() == "" -> {
                     bgPhone.error = "This field is empty"
                     bgPhone.requestFocus()
                 }

                 else -> {

                     val notification = JSONObject()
                     val notificationBody = JSONObject()
                     try {
                         notificationBody.put("title", "Urgently Required")
                         notificationBody.put(
                             "message", "Name: " + bgName.text.toString() +
                                     "\nAge: " + bgAge.text.toString() +
                                     "\nContact: " + bgPhone.text.toString() +
                                     "\nBlood Group: " + bloodGrp +
                                     "\nDescription: " + bgDes.text.toString()
                         )
                         notification.put("to", "/topics/all")
                         notification.put("data", notificationBody)

                         bgAge.setText(" ")
                         bgName.setText(" ")
                         bgDes.setText(" ")

                     } catch (e: JSONException) {
                         Log.e("TAG", "onCreate: " + e.message)
                     }
                     sendNotification(notification)
                 }
             }
         }
     }

     private fun sendNotification(notification: JSONObject) {

         val tag = MainActivity::class.java.simpleName

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
         MySingleton.instance?.addToRequestQueue(jsonObjectRequest, tag)
     }
}

