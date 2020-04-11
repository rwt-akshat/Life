package com.akrwt.arogya.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.akrwt.arogya.MainActivity
import com.akrwt.arogya.R
import com.akrwt.docsapp.MySingleton
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.fragment_bloodgrp.view.*
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception


class BloodReqFragment : Fragment() {

    private var SHARED_PREFS = "sharedPrefs"
    private var PHONE = "phone"

    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
        "key=" + "AAAAZu9fBbE:APA91bHH1ejeiLczM49J4PmPI6eM5suCkgiJa3scqSsd0sXVV78zoIxBvmtAv_2qlzlgF425JbYZ4uRXxKf8XgZ2A7cTKNEWDnYxZ3409-PWUjofF5fhGhQsqOfHkP4XTQjPCcm_70dr"
    private val contentType = "application/json"


    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bloodgrp, container, false)

        activity!!.title = "Blood Group Required"
        val sharedRef = context!!.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        var phn = sharedRef.getString(PHONE, "DEFAULT")

        view.BGPhone.setText(phn)
        view.BGPhone.keyListener = null
        view.BGPhone.isEnabled = false

        sendDetails(view)


        return view
    }

    private fun sendDetails(v: View) {

        v.btnSend.setOnClickListener {

            when {
                v.BGAge.text.toString() == "" -> {
                    v.BGAge.error = "This field is empty"
                    v.BGAge.requestFocus()
                }
                v.BGName.text.toString() == "" -> {
                    v.BGName.error = "This field is empty"
                    v.BGName.requestFocus()
                }
                v.BGPhone.text.toString() == "" -> {
                    v.BGPhone.error = "This field is empty"
                    v.BGPhone.requestFocus()
                }
                v.BGRq.text.toString() == "" -> {
                    v.BGRq.error = "This field is empty"
                    v.BGRq.requestFocus()
                }
                else -> {

                    val notification = JSONObject()
                    val notificationBody = JSONObject()
                    try {
                        notificationBody.put("title", "Urgently Required")
                        notificationBody.put(
                            "message", "Name: " + v.BGName.text.toString() +
                                    "\nAge: " + v.BGAge.text.toString() +
                                    "\nContact: " + v.BGPhone.text.toString() +
                                    "\nBlood Group: " + v.BGRq.text.toString() +
                                    "\nDescription: " + v.BGDes.text.toString()
                        )
                        notification.put("to", "/topics/all")
                        notification.put("data", notificationBody)

                        v.BGAge.setText(" ")
                        v.BGName.setText(" ")
                        v.BGDes.setText(" ")
                        v.BGRq.setText(" ")

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
}

