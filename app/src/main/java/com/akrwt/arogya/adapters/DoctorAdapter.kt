package com.akrwt.arogya.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.R
import com.akrwt.arogya.utils.DoctorModel
import com.squareup.picasso.Picasso

class DoctorAdapter(
    private var mContext: Context,
    private val Doctors: ArrayList<DoctorModel>,
    private val navController: NavController
) :
    RecyclerView.Adapter<DoctorAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textViewName: TextView? = null
        internal var imageView: ImageView? = null

        init {
            this.textViewName = itemView.findViewById(R.id.doctor_text)
            this.imageView = itemView.findViewById(R.id.doctor_image) as ImageView
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.doctor_ticket, parent, false)

        return MyViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        val textViewName = holder.textViewName
        val imageView = holder.imageView

        textViewName!!.text = (Doctors[listPosition].dName)

        Picasso.get()
            .load(Doctors[listPosition].image).into(imageView)

        holder.itemView.setOnClickListener {
            navController.navigate(R.id.askDoctorDetailsFragment)
        }
    }

    override fun getItemCount(): Int {
        return Doctors.size
    }
}
