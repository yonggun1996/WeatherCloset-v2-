package com.example.example2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_out1viewholder.view.*

class Fragment1OutViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var view : View = v

    fun bind(item : Fragment1OutData){
        view.closet_tv.text = item.closet
    }
}