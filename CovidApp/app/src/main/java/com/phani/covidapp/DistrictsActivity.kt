package com.phani.covidapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.phani.covidapp.adapter.DistrictsAdapter
import com.phani.covidapp.databinding.ActivityDistrictsBinding
import org.json.JSONObject

class DistrictsActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityDistrictsBinding
    private var districtCases = mutableListOf<Model>()
    private lateinit var mAdapter: DistrictsAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDistrictsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val currentStateName = intent.getStringExtra("currentStateClicked") ?: ""
        mBinding.stateNameTvD.text = intent.getStringExtra("currentStateName")

        val recyclerView = mBinding.districtCasesRecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter = DistrictsAdapter(districtCases)
        recyclerView.adapter = mAdapter

        getStateCovidInfo(currentStateName)

        mAdapter.notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getStateCovidInfo(curState: String) {
        val curObject = JSONObject(curState)
        val districtsOfState = curObject.keys()

        while (districtsOfState.hasNext()) {
            val curDistrict = districtsOfState.next()
            val curDistrictObject = curObject.getJSONObject(curDistrict)

            val curItem = Model(
                curDistrict,
                curDistrictObject.getInt("active"),
                curDistrictObject.getInt("confirmed"),
                curDistrictObject.getInt("deceased"),
                curDistrictObject.getInt("recovered"),
                ""
            )
            districtCases.add(curItem)
            mAdapter.notifyDataSetChanged()
        }
    }
}
