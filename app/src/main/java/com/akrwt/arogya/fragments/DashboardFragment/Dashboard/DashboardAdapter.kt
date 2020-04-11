package com.akrwt.cr.ui

import android.content.Context
import android.content.Intent
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.fragments.DashboardFragment.Impact.AccidentActivity
import com.akrwt.arogya.R
import com.akrwt.arogya.fragments.DashboardFragment.Coronavirus.CoronavirusActivity
import com.akrwt.arogya.fragments.DashboardFragment.Impact.ImpactActivity
import com.squareup.picasso.Picasso

class DashboardAdapter(
    private var mContext: Context,
    private val diseases: ArrayList<Model>
) :
    RecyclerView.Adapter<DashboardAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textViewName: TextView? = null
        internal var imageView: ImageView? = null

        init {
            this.textViewName = itemView.findViewById(R.id.disease_text)
            this.imageView = itemView.findViewById(R.id.disease_image) as ImageView
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.disease_ticket, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {

        val textViewName = holder.textViewName
        val imageView = holder.imageView

        textViewName!!.text = (diseases[listPosition].crime_name)

        Picasso.get()
            .load(diseases[listPosition].image).into(imageView)

        when (textViewName.text) {
            "Vehicle Impact" -> {
                imageView!!.setImageResource(R.drawable.ic_crash)
            }
            "Accident" -> {
                imageView!!.setImageResource(R.drawable.ic_accident)
            }
            else -> {
                imageView!!.setImageResource(R.drawable.covid)
            }
        }

        holder.itemView.setOnClickListener {

            when (textViewName.text) {
                "Accident" -> {
                    val intent = Intent(mContext, AccidentActivity::class.java)
                    intent.putExtra("name", textViewName.text.toString())

                    mContext.startActivity(intent)
                }
                "Vehicle Impact" -> {
                    val intent = Intent(mContext, ImpactActivity::class.java)
                    intent.putExtra("name", textViewName.text.toString())
                    mContext.startActivity(intent)
                }
                else -> {
                    val intent = Intent(mContext, CoronavirusActivity::class.java)
                    intent.putExtra("name", textViewName.text.toString())
                    mContext.startActivity(intent)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return diseases.size
    }
}
