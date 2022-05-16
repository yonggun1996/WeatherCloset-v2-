package com.example.example2.BottomNavigationFragment1

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.example2.R

class Fragment1OutAdapter(val itemList : ArrayList<Fragment1OutData>) : RecyclerView.Adapter<Fragment1OutViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Fragment1OutViewHolder {
        val inflatedView = LayoutInflater.from(p0.context)
            .inflate(R.layout.activity_out1viewholder,p0,false)

        return Fragment1OutViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(p0: Fragment1OutViewHolder, p1: Int) {
        val item = itemList[p1]
        p0.apply {
            bind(item)
        }
    }

}