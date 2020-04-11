package com.akrwt.arogya.fragments.DashboardFragment.Coronavirus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.akrwt.arogya.R
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.json.JSONArray

class CountriesData : AppCompatActivity() {

    lateinit var mChart: HorizontalBarChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_countries_data)
        supportActionBar!!.hide()
        if (intent.getStringExtra("case") == "confirmed") {
            getConfirmedData()
        }
        else if (intent.getStringExtra("case") == "active") {
            getActiveData()
        }
        else if (intent.getStringExtra("case") == "deaths") {
            getDeathsData()
        }
        if (intent.getStringExtra("case") == "recovered") {
            getRecoveredData()
        }
    }

    private fun getConfirmedData() {
        val countries: ArrayList<String> = ArrayList()
        val confirmedList:ArrayList<Float> = ArrayList()
        val request = StringRequest(
            Request.Method.GET,
            "https://corona.lmao.ninja/countries",
            Response.Listener {
                val jArray = JSONArray(it)
                for (i in 0 until jArray.length()) {
                    val jsonObj = jArray.getJSONObject(i)
                    val country = jsonObj.getString("country")
                    countries.add(country)

                    val confirmed = jsonObj.getString("cases")
                    confirmedList.add(confirmed.toFloat())
                }

                mChart = findViewById(R.id.hBarChart)

                val c = ArrayList<BarEntry>()
                for(j in 0 until confirmedList.size) {
                    c.add(BarEntry(confirmedList[j], j))
                }

                val barDataSet = BarDataSet(c, "Confirmed")
                mChart.animateY(3000)
                val data = BarData(countries, barDataSet)
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                mChart.data = data

            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun getActiveData() {
        val countries: ArrayList<String> = ArrayList()
        val activeList:ArrayList<Float> = ArrayList()


        val request = StringRequest(
            Request.Method.GET,
            "https://corona.lmao.ninja/countries",
            Response.Listener {
                val jArray = JSONArray(it)
                for (i in 0 until jArray.length()) {
                    val jsonObj = jArray.getJSONObject(i)
                    val country = jsonObj.getString("country")
                    countries.add(country)

                    val confirmed = jsonObj.getString("active")
                    activeList.add(confirmed.toFloat())
                }

                Log.e("active", activeList.toString())

                mChart = findViewById(R.id.hBarChart)

                val c = ArrayList<BarEntry>()
                for(j in 0 until activeList.size) {
                    c.add(BarEntry(activeList[j], j))
                }

                val barDataSet = BarDataSet(c, "Active")
                mChart.animateY(3000)
                val data = BarData(countries, barDataSet)
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                mChart.data = data

            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun getRecoveredData() {
        val countries: ArrayList<String> = ArrayList()
        val recoveredList:ArrayList<Float> = ArrayList()

        val request = StringRequest(
            Request.Method.GET,
            "https://corona.lmao.ninja/countries",
            Response.Listener {
                val jArray = JSONArray(it)
                for (i in 0 until jArray.length()) {
                    val jsonObj = jArray.getJSONObject(i)
                    val country = jsonObj.getString("country")
                    countries.add(country)

                    val confirmed = jsonObj.getString("recovered")
                    recoveredList.add(confirmed.toFloat())
                }

                Log.e("recovered", recoveredList.toString())

                mChart = findViewById(R.id.hBarChart)

                val c = ArrayList<BarEntry>()
                for(j in 0 until recoveredList.size) {
                    c.add(BarEntry(recoveredList[j], j))
                }

                val barDataSet = BarDataSet(c, "Recovered")
                mChart.animateY(3000)
                val data = BarData(countries, barDataSet)
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                mChart.data = data


            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(this).add(request)
    }

    private fun getDeathsData() {
        val countries: ArrayList<String> = ArrayList()
        val deathList:ArrayList<Float> = ArrayList()


        val request = StringRequest(
            Request.Method.GET,
            "https://corona.lmao.ninja/countries",
            Response.Listener {
                val jArray = JSONArray(it)
                for (i in 0 until jArray.length()) {
                    val jsonObj = jArray.getJSONObject(i)
                    val country = jsonObj.getString("country")
                    countries.add(country)

                    val confirmed = jsonObj.getString("deaths")
                    deathList.add(confirmed.toFloat())
                }

                Log.e("deaths", deathList.toString())

                mChart = findViewById(R.id.hBarChart)

                val c = ArrayList<BarEntry>()
                for(j in 0 until deathList.size) {
                    c.add(BarEntry(deathList[j], j))
                }

                val barDataSet = BarDataSet(c, "Deaths")
                mChart.animateY(3000)
                val data = BarData(countries, barDataSet)
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
                mChart.data = data

            },
            Response.ErrorListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
            })
        Volley.newRequestQueue(this).add(request)
    }
}
