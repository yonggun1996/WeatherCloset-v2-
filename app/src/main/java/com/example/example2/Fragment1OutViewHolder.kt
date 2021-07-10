package com.example.example2

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_out1viewholder.*
import kotlinx.android.synthetic.main.activity_out1viewholder.view.*

class Fragment1OutViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    var view : View = v

    fun bind(item : Fragment1OutData){
        //추가로 네이버 쇼핑에 대한 리싸이클러 뷰를 가져온다
        view.closet_tv.text = item.closet
        var adapter = NaverApiAdapter(item.naverlist)
        view.inrecyclerview?.setHasFixedSize(true)
        view.inrecyclerview?.adapter = adapter
        view.inrecyclerview?.layoutManager = LinearLayoutManager(view.context).also {
            it.orientation = LinearLayoutManager.HORIZONTAL
        }
    }
}