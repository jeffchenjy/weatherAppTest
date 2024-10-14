package com.example.myrecyclerviewhelper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

import com.example.weatherapp.R

class CityRecyclerViewAdapter(private var basicView: View?, private var context: Context?, private var activity: FragmentActivity?,
                              private var dataList: List<Pair<String, String>>) : RecyclerView.Adapter<CityRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ItemClickSupportViewHolder {
        val item_name: TextView = itemView.findViewById(R.id.item_name)
        override val isLongClickable: Boolean get() = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.recyclerview_city,
                    parent,
                    false
                )
        )
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (resourceId, locationName) = dataList[position]
        holder.item_name.text = locationName
    }
    override fun getItemCount() = dataList.size

    fun getItem(position: Int): Pair<String, String> {
        return dataList[position]
    }
}