package com.phani.covidapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.phani.covidapp.DistrictsActivity
import com.phani.covidapp.Model
import com.phani.covidapp.R

class StatesAdapter(private val listOfStates: List<Model>) : RecyclerView.Adapter<StatesAdapter.StateHolder>() {

    class StateHolder(view: View) : RecyclerView.ViewHolder(view) {
        val activeCasesTV: TextView = view.findViewById(R.id.activeCasesTV)
        val confirmedCasesTV: TextView = view.findViewById(R.id.confirmedCasesTV)
        val recoveredCasesTV: TextView = view.findViewById(R.id.recoveredCasesTV)
        val deceasedCasesTV: TextView = view.findViewById(R.id.deceasesCasesTV)
        val placeNameTV: TextView = view.findViewById(R.id.placeNameTV)
        val curStateItemView: ConstraintLayout = view.findViewById(R.id.curStateItemCL)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateHolder {
        return StateHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.testing, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StateHolder, position: Int) {
        val curStateItem = listOfStates[position]

        holder.activeCasesTV.text = curStateItem.activeCases.toString()
        holder.confirmedCasesTV.text = curStateItem.confirmedCases.toString()
        holder.recoveredCasesTV.text = curStateItem.recoveredCases.toString()
        holder.deceasedCasesTV.text = curStateItem.deceasedCases.toString()
        holder.placeNameTV.text = curStateItem.name

        holder.curStateItemView.setOnClickListener {
            Toast.makeText(
                holder.itemView.context,
                "Displaying ${curStateItem.name}'s detailed information",
                Toast.LENGTH_SHORT
            ).show()

            val intent = Intent(holder.itemView.context, DistrictsActivity::class.java)
            intent.putExtra("currentStateClicked", curStateItem.curStateDetailsJSON)
            intent.putExtra("currentStateName", curStateItem.name)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listOfStates.size
    }
}