package com.akrwt.arogya.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.R
import com.akrwt.arogya.adapters.AmbulanceAdapter
import com.akrwt.docsapp.Fragments.AmbulanceModel
import com.google.firebase.database.*

class AmbulanceFragment : Fragment() {

    var mAdapter: AmbulanceAdapter? = null
    var mDatabaseRef: DatabaseReference? = null
    var mUploads: ArrayList<AmbulanceModel>? = null
    private lateinit var progressBar:ProgressBar
    private lateinit var errorTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_ambulance, container, false)

        val mRecyclerView: RecyclerView = v.findViewById(R.id.rv_amb)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        progressBar = v.findViewById(R.id.ambulanceProgress)


        mUploads = ArrayList()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ambulance")
        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                mUploads!!.clear()
                var testArray: ArrayList<AmbulanceModel>? = null

                for (postSnapshot in dataSnapshot.children) {
                    val upload = postSnapshot.getValue(AmbulanceModel::class.java)
                    mUploads!!.add(upload!!)

                    testArray = mUploads

                }

                if(context!=null){
                    if (testArray != null) {
                        progressBar.visibility = View.GONE
                        mAdapter =
                            AmbulanceAdapter(
                                context!!,
                                mUploads!!
                            )
                        mRecyclerView.adapter = mAdapter
                        mAdapter!!.notifyDataSetChanged()

                    } else {
                        progressBar.visibility = View.GONE
                        errorTextView.visibility = View.VISIBLE
                    }
                }

            }
        })
        return v
    }
}