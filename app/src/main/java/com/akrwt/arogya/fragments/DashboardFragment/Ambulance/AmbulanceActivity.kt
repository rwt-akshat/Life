package com.akrwt.arogya.fragments.DashboardFragment.Ambulance

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.R
import com.akrwt.docsapp.Fragments.AmbulanceModel
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_ambulance.*

class AmbulanceActivity : AppCompatActivity() {

    var mAdapter: AmbulanceAdapter? = null
    var mDatabaseRef: DatabaseReference? = null
    var mUploads: ArrayList<AmbulanceModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ambulance)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CALL_PHONE), 123
            )
        }

        supportActionBar!!.title = "Available Ambulance"

        val mRecyclerView: RecyclerView = findViewById(R.id.rv_amb)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)


        mUploads = ArrayList()
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("ambulance")
        mDatabaseRef!!.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                mUploads!!.clear()
                var testArray: ArrayList<AmbulanceModel>? = null

                for (postSnapshot in dataSnapshot.children) {
                    val upload = postSnapshot.getValue(AmbulanceModel::class.java)
                    mUploads!!.add(upload!!)

                    testArray = mUploads

                }

                if (testArray != null) {
                    ambulanceProgress.visibility = View.GONE
                    mAdapter =
                        AmbulanceAdapter(
                            applicationContext,
                            mUploads!!
                        )
                    mRecyclerView.adapter = mAdapter
                    mAdapter!!.notifyDataSetChanged()

                } else {
                    ambulanceProgress.visibility = View.GONE
                    textNothing.visibility = View.VISIBLE
                }
            }
        })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 123) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(applicationContext, "PERMISSION DENIED", Toast.LENGTH_LONG).show()
            }
        }
    }
}
