package com.akrwt.cr.ui

import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.MapsActivity
import com.akrwt.arogya.R
import com.squareup.picasso.Picasso

class DashboardAdapter(
    private var mContext: Context,
    private val dashboard: ArrayList<DashboardModel>,
    private val navController: NavController
) :
    RecyclerView.Adapter<DashboardAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textViewName: TextView? = null
        internal var imageView: ImageView? = null
        internal var textViewDes: TextView? = null

        init {
            this.textViewName = itemView.findViewById(R.id.dashboard_name)
            this.imageView = itemView.findViewById(R.id.dashboard_image)
            this.textViewDes = itemView.findViewById(R.id.card_text)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dashboard_ticket, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        val current = dashboard[listPosition]
        holder.textViewName!!.text = current.name
        holder.textViewDes!!.text = current.des
        Picasso.get().load(dashboard[listPosition].image).into(holder.imageView)

        holder.itemView.setOnClickListener {

            when (holder.textViewName!!.text) {
                "Accident" -> {
                    navController.navigate(R.id.accidentFragment)
                }
                "Vehicle Impact" -> {
                    navController.navigate(R.id.vehicleImpactFragment)
                }
                "Coronavirus" -> {
                    navController.navigate(R.id.coronavirusFragment)
                }
                "Know Your Location" -> {
                    mContext.startActivity(Intent(mContext, MapsActivity::class.java))
                }
                "About Us" -> {
                    navController.navigate(R.id.aboutFragment)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dashboard.size
    }
}
