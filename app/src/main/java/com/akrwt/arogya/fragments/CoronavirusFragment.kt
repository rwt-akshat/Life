package com.akrwt.arogya.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.akrwt.arogya.R
import com.akrwt.arogya.activity.CountriesDataActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_coronavirus.view.*
import org.json.JSONObject

class CoronavirusFragment : Fragment() {

    private lateinit var barChart: BarChart

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_coronavirus, container,false)
        barChart = v.findViewById(R.id.barGraph)

        if(context!=null){
            getData(v)
            individualActivity(v)
        }

        v.fab.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+911123978046")
            startActivity(intent)
        }
        return v
    }
    private fun individualActivity(v:View) {
        v.confirmed.setOnClickListener {
            val i = Intent(context,
                CountriesDataActivity::class.java)
            i.putExtra("case", "confirmed")
            startActivity(i)
        }
        v.active.setOnClickListener {
            val i = Intent(context,
                CountriesDataActivity::class.java)
            i.putExtra("case", "active")
            startActivity(i)
        }
        v.deaths.setOnClickListener {
            val i = Intent(context,
                CountriesDataActivity::class.java)
            i.putExtra("case", "deaths")
            startActivity(i)
        }
        v.recovered.setOnClickListener {
            val i = Intent(context,
                CountriesDataActivity::class.java)
            i.putExtra("case", "recovered")
            startActivity(i)
        }
    }
    private fun getData(v:View) {
        val request = StringRequest(
            Request.Method.GET,
            "https://corona.lmao.ninja/v2/all",
            Response.Listener {
                val jsonObject = JSONObject(it)

                val cases = jsonObject.getString("cases").toFloat()
                val active = jsonObject.getString("active").toFloat()
                val deaths = jsonObject.getString("deaths").toFloat()
                val recovered = jsonObject.getString("recovered").toFloat()

                val noOfEmp = ArrayList<BarEntry>()
                noOfEmp.add(BarEntry(cases, 0))
                noOfEmp.add(BarEntry(active, 1))
                noOfEmp.add(BarEntry(recovered, 2))
                noOfEmp.add(BarEntry(deaths, 3))

                val case = ArrayList<String>()

                case.add("Confirmed")
                case.add("Active")
                case.add("Recovered")
                case.add("Deaths")

                v.pBar.visibility = View.INVISIBLE
                v.txtView.visibility = View.INVISIBLE

                val barDataSet = BarDataSet(noOfEmp, "Cases")
                barChart.animateY(3000)
                val data = BarData(case, barDataSet)
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                barChart.data = data

            },
            Response.ErrorListener {
                v.pBar.visibility = View.INVISIBLE
                v.txtView.visibility = View.INVISIBLE
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(requireContext()).add(request)
    }
}