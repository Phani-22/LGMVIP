package com.phani.covidapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.phani.covidapp.Model
import com.phani.covidapp.R

class DistrictsAdapter(private var listOfDistricts: List<Model>) :
    RecyclerView.Adapter<DistrictsAdapter.DistrictHolder>() {

    class DistrictHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activeCasesTV: TextView = view.findViewById(R.id.activeCasesTV)
        val confirmedCasesTv: TextView = view.findViewById(R.id.confirmedCasesTV)
        val recoveredCasesTV: TextView = view.findViewById(R.id.recoveredCasesTV)
        val deceasedCasesTV: TextView = view.findViewById(R.id.deceasesCasesTV)
        val placeNameTV: TextView = view.findViewById(R.id.placeNameTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictHolder {
        return DistrictHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.testing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DistrictHolder, position: Int) {
        val curDistrictItem = listOfDistricts[position]

        holder.activeCasesTV.text = curDistrictItem.activeCases.toString()
        holder.confirmedCasesTv.text = curDistrictItem.confirmedCases.toString()
        holder.recoveredCasesTV.text = curDistrictItem.recoveredCases.toString()
        holder.deceasedCasesTV.text = curDistrictItem.deceasedCases.toString()
        holder.placeNameTV.text = curDistrictItem.name
    }

    override fun getItemCount(): Int {
        return listOfDistricts.size
    }
}