package com.akrwt.arogya.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.R
import com.akrwt.cr.ui.DashboardModel
import com.akrwt.cr.ui.DashboardAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DashBoardFragment : Fragment() {

    private var requestCall = 1
    private lateinit var navController:NavController

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val fab = view.findViewById(R.id.fab) as FloatingActionButton
        navController = requireParentFragment().findNavController()

        fab.setOnClickListener {

            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    android.Manifest.permission.CALL_PHONE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
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


    private fun loadView(view: View) {

        val dashboard = ArrayList<DashboardModel>()

        dashboard.add(DashboardModel("Accident", R.drawable.ic_crash, "Someone had Accident"))
        dashboard.add(DashboardModel("Vehicle Impact", R.drawable.ic_accident, "Damaged Vehicle"))
        dashboard.add(DashboardModel("Coronavirus",R.drawable.covid,"Covid 19 related information"))
        dashboard.add(DashboardModel("Know Your Location",R.drawable.ic_dashboard_location, "Lost somewhere"))
        dashboard.add(DashboardModel("About Us", R.drawable.about_app, "About life app"))

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)

        recyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(context,2)
        recyclerView.layoutManager = gridLayoutManager

        recyclerView.adapter = DashboardAdapter(requireContext(), dashboard, navController)

    }
}

