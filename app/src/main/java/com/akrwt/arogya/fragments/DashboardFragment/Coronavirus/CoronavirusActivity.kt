package com.akrwt.arogya.fragments.DashboardFragment.Coronavirus

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.akrwt.arogya.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_coronavirus.*
import org.json.JSONObject

class CoronavirusActivity : AppCompatActivity() {

    lateinit var barChart: BarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coronavirus)
        supportActionBar!!.hide()
        barChart = findViewById(R.id.barGraph)

        getData()
        individualActivity()

        fab.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:+911123978046")
            startActivity(intent)
        }

    }

    private fun individualActivity() {
        confirmed.setOnClickListener {
            val i = Intent(this,
                CountriesData::class.java)
            i.putExtra("case", "confirmed")
            startActivity(i)
        }
        active.setOnClickListener {
            val i = Intent(this,
                CountriesData::class.java)
           i.putExtra("case", "active")
            startActivity(i)
        }
        deaths.setOnClickListener {
            val i = Intent(this,
                CountriesData::class.java)
           i.putExtra("case", "deaths")
            startActivity(i)
        }
        recovered.setOnClickListener {
            val i = Intent(this,
                CountriesData::class.java)
            i.putExtra("case", "recovered")
            startActivity(i)
        }
    }

    private fun getData() {
        val request = StringRequest(
            Request.Method.GET,
            "https://corona.lmao.ninja/all",
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

                pBar.visibility = View.INVISIBLE
                txtView.visibility = View.INVISIBLE

                val barDataSet = BarDataSet(noOfEmp, "Cases")
                barChart.animateY(3000)
                val data = BarData(case, barDataSet)
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                barChart.data = data

            },
            Response.ErrorListener {
                pBar.visibility = View.INVISIBLE
                txtView.visibility = View.INVISIBLE
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(this).add(request)
    }
}