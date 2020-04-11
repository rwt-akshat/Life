package com.akrwt.arogya.fragments.DashboardFragment.Dashboard

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.R
import com.akrwt.cr.ui.Model
import com.akrwt.cr.ui.DashboardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DashBoardFragment : Fragment() {

    private var requestCall = 1
    private var flip:ViewFlipper?=null

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val fab = view.findViewById(R.id.fab) as FloatingActionButton
        flip=view.findViewById(R.id.v_flipper) as ViewFlipper
        activity!!.title = "Dashboard"

        var arr= arrayOf(R.drawable.em1,R.drawable.em2,R.drawable.em3,R.drawable.em5,R.drawable.em6)


        for(element in arr){
            flipperImages(element)
        }




        fab.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    android.Manifest.permission.CALL_PHONE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(android.Manifest.permission.CALL_PHONE), requestCall
                )
            } else {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:911")
                startActivity(intent)
            }
        }

        loadView(view)
        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestCall) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(activity, "Welcome to Life App", Toast.LENGTH_LONG)
                    .show()

            } else {
                Toast.makeText(activity, "PERMISSION DENIED", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun flipperImages(images:Int){
        val imgView= ImageView(context)
        imgView.setBackgroundResource(images)

        flip!!.addView(imgView)
        flip!!.flipInterval=4000
        flip!!.isAutoStart=true

        flip!!.setInAnimation(context,android.R.anim.slide_in_left)
        flip!!.setOutAnimation(context,android.R.anim.slide_out_right)
    }

    private fun loadView(view: View) {

        val listofdiseases = ArrayList<Model>()

        listofdiseases.add(Model("Accident", R.drawable.background))
        listofdiseases.add(Model("Vehicle Impact", R.drawable.background))
        listofdiseases.add(Model("Coronavirus",R.drawable.background))

        val recyclerView: RecyclerView =
            view.findViewById(R.id.recyclerView) as RecyclerView

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = DashboardAdapter(context!!, listofdiseases)

    }
}

