package com.akrwt.arogya

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.fragments.DoctorsFragment.DoctorAdapter
import com.akrwt.arogya.fragments.DoctorsFragment.DoctorModel

class DoctorsFragment : Fragment() {

    @Nullable
    @Override
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_emergency_main, container, false)

        activity!!.title = "Dashboard"

        loadView(view)
        return view
    }

    private fun loadView(view: View) {

        val listOfDoctors = ArrayList<DoctorModel>()

        listOfDoctors.add(
            DoctorModel(
                "Neurologist",
                R.drawable.ic_neuro
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Dermatologist",
                R.drawable.ic_derm
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Physiotherapist",
                R.drawable.ic_phy
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Ophthalmologist",
                R.drawable.ophthalmologist
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Cardiologist",
                R.drawable.cardiologist
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Gastroenterologist",
                R.drawable.gastroenterologist
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Nephrologist",
                R.drawable.kidney
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Urologists",
                R.drawable.urology
            )
        )
        listOfDoctors.add(
            DoctorModel(
                "Pulmonologist",
                R.drawable.pulmonologist
            )
        )

        val recyclerView: RecyclerView =
            view.findViewById(R.id.recyclerView2) as RecyclerView

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter =
            DoctorAdapter(
                context!!,
                listOfDoctors
            )

    }
}


