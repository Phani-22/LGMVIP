package com.phani.covidapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request.Method.GET
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.phani.covidapp.adapter.StatesAdapter
import com.phani.covidapp.databinding.ActivityMainBinding
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var listOfStates: ArrayList<Model>
    private lateinit var mAdapter: StatesAdapter
    private var doubleBackToExitPressedOnce = false

    companion object {
        private const val url = "https://data.covid19india.org/state_district_wise.json"
        private const val TAG = "GetStatesCases"
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        listOfStates = ArrayList()
        mAdapter = StatesAdapter(listOfStates)
        val recyclerView = mBinding.stateCasesRecyclerView

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter

        getStateCovidInfo()

        mAdapter.notifyDataSetChanged()

    }

    // getting from api
    @SuppressLint("NotifyDataSetChanged")
    private fun getStateCovidInfo() {
        mRequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest = JsonObjectRequest(
            GET, url, null,
            {
                val namesOfStates = it.keys()
                while (namesOfStates.hasNext()) {
                    val currentStateName = namesOfStates.next().toString()
                    try {
                        if (currentStateName == "State Unassigned") continue
                        val curStateObject =
                            it.getJSONObject(currentStateName).getJSONObject("districtData")
                        val districtsOfCurState = curStateObject.keys()

                        var activeCasesOfCurState = 0
                        var confirmedCasesOfCurState = 0
                        var deceasedCasesOfCurState = 0
                        var recoveredCasesOfCurState = 0

                        while (districtsOfCurState.hasNext()) {
                            val curDistrictName = districtsOfCurState.next().toString()
                            val curDistrictObject = curStateObject.getJSONObject(curDistrictName)

                            activeCasesOfCurState += Integer.parseInt(curDistrictObject.getString("active"))
                            confirmedCasesOfCurState += Integer.parseInt(
                                curDistrictObject.getString(
                                    "confirmed"
                                )
                            )
                            deceasedCasesOfCurState += Integer.parseInt(
                                curDistrictObject.getString(
                                    "deceased"
                                )
                            )
                            recoveredCasesOfCurState += Integer.parseInt(
                                curDistrictObject.getString(
                                    "recovered"
                                )
                            )
                        }

                        val curStateModelObject =
                            Model(
                                currentStateName,
                                activeCasesOfCurState,
                                confirmedCasesOfCurState,
                                deceasedCasesOfCurState,
                                recoveredCasesOfCurState,
                                curStateObject.toString()
                            )
                        listOfStates.add(curStateModelObject)
                        mAdapter.notifyDataSetChanged()

                    } catch (e: JSONException) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            }, {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                it.printStackTrace()
            })

        jsonObjectRequest.tag = TAG
        mRequestQueue.add(jsonObjectRequest)
    }

    override fun onStop() {
        super.onStop()
        mRequestQueue.cancelAll(TAG)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        mAdapter.notifyDataSetChanged()
        super.onResume()
    }

    // working for double tap to exit
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}