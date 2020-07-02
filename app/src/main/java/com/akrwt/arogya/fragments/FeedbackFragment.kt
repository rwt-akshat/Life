package com.akrwt.arogya.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.akrwt.arogya.R

class FeedbackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_feedback, container, false)
        val sendBtn:Button = v.findViewById(R.id.sendBtn)
        val feedbackET:EditText = v.findViewById(R.id.etFeedback)

        sendBtn.setOnClickListener {
            val feedback = feedbackET.text.toString()

            if (feedback.isEmpty()) {
                Toast.makeText(requireContext(), "Feedback field is empty", Toast.LENGTH_SHORT).show()
            } else {
                if (feedback.length < 10) {
                    Toast.makeText(
                        requireContext(),
                        "Feedback should be of length greater than 10 .. ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val email = Intent(Intent.ACTION_SENDTO)
                    email.data = Uri.parse("mailto:")
                    email.putExtra(Intent.EXTRA_EMAIL, arrayOf("akshatrwt00@gmail.com"))
                    email.putExtra(Intent.EXTRA_TEXT, feedback)
                    email.putExtra(Intent.EXTRA_SUBJECT, "FEEDBACK")
                    startActivity(Intent.createChooser(email, "Choose an email client :"))
                }
            }
        }
        return v
    }
}