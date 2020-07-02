package com.akrwt.arogya.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.akrwt.arogya.R

class AddFriendsFragment : Fragment() {

    private var sharedPreferences: SharedPreferences? = null
    private lateinit var firstName:EditText
    private lateinit var secondName:EditText
    private lateinit var thirdName:EditText

    private lateinit var firstNumber:EditText
    private lateinit var secondNumber:EditText
    private lateinit var thirdNumber:EditText
    private lateinit var buttonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_friends, container, false)

        firstName = view.findViewById(R.id.etName1)
        secondName = view.findViewById(R.id.etName2)
        thirdName = view.findViewById(R.id.etName3)
        firstNumber = view.findViewById(R.id.etPhone1)
        secondNumber = view.findViewById(R.id.etPhone2)
        thirdNumber = view.findViewById(R.id.etPhone3)
        buttonSave = view.findViewById(R.id.btnSave)





        sharedPreferences = context!!.getSharedPreferences("FriendNames", Context.MODE_PRIVATE)
        buttonSave.setOnClickListener {

            when {
                firstName.text.toString().trim().isEmpty() -> {
                    firstName.error = "This field cannot be empty.."
                }
                secondName.text.toString().trim().isEmpty() -> {
                    secondName.error = "This field cannot be empty.."
                }
                thirdName.text.toString().trim().isEmpty() -> {
                    thirdName.error = "This field cannot be empty.."
                }
                firstNumber.text.toString().trim().isEmpty() -> {
                    firstNumber.error = "This field cannot be empty.."
                }
                secondNumber.text.toString().trim().isEmpty() -> {
                    secondNumber.error = "This field cannot be empty.."
                }
                thirdNumber.text.toString().trim().isEmpty() -> {
                    thirdNumber.error = "This field cannot be empty.."
                }
                else -> {


                    val editor = sharedPreferences!!.edit()
                    editor.putString("name1", firstName.text.toString())
                    editor.putString("name2", secondName.text.toString())
                    editor.putString("name3", thirdName.text.toString())
                    editor.putString("phone1", firstNumber.text.toString())
                    editor.putString("phone2", secondNumber.text.toString())
                    editor.putString("phone3", thirdNumber.text.toString())
                    editor.apply()

                    Toast.makeText(context, "Details Saved", Toast.LENGTH_LONG)
                        .show()

                    // startActivity(Intent(this, ImpactActivity::class.java))
                }
            }
        }
        return view
    }
}