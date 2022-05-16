package com.example.example2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.example2.BottomNavigationFragment1.NaverApiData

class MoresearchAdapter(val itemList : ArrayList<NaverApiData>) :
    RecyclerView.Adapter<NaverApiViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NaverApiViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.activity_inviewholder,parent,false)

        return NaverApiViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: NaverApiViewHolder, position: Int) {
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}