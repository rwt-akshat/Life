package com.akrwt.arogya.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akrwt.arogya.R
import com.akrwt.docsapp.Fragments.AmbulanceModel


class AmbulanceAdapter(
    var mContext: Context,
    private var mUploads: ArrayList<AmbulanceModel>
) :
    RecyclerView.Adapter<AmbulanceAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var textViewName: TextView? = null
        internal var textViewCount: TextView? = null
        internal var textViewContact: TextView? = null
        internal var imageView: ImageView? = null


        init {
            this.textViewName = itemView.findViewById(R.id.tvName)
            this.textViewCount = itemView.findViewById(R.id.tvCount)
            this.textViewContact = itemView.findViewById(R.id.tvContact)
            this.imageView = itemView.findViewById(R.id.ivcross)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(mContext).inflate(R.layout.items, parent, false)


        return MyViewHolder(
            v
        )

    }

    override fun getItemCount(): Int {
        return mUploads.size

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val uploadCurrent = mUploads.get(position)

        var Hname = uploadCurrent.getName()
        var c = uploadCurrent.getAmb()
        var contact = uploadCurrent.getContact()

        holder.textViewName!!.text = "Hospital's Name: " + Hname
        holder.textViewCount!!.text = "Number of Ambulance: " + c
        holder.textViewContact!!.text = "Contact: " + contact





        if (c.toInt() > 0) {
            holder.imageView!!.setImageResource(R.drawable.ic_tick)
            holder.itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$contact")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                mContext.startActivity(intent)

            }

        }


    }


}