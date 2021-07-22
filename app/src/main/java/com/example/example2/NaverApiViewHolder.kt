package com.example.example2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_inviewholder.view.*

class NaverApiViewHolder(v : View) : RecyclerView.ViewHolder(v) {
    var view : View = v

    fun bind(item: NaverApiData){
        var url = item.image_url
        Glide.with(view.context)
            .load(url)
            .into(view.naver_img)

        //이미지 버튼을 클릭해서 웹 페이지로 넘길 예정
        view.naver_img.setOnClickListener {
            println("이미지 버튼 클릭")
        }

        view.naver_title.text = item.title
        view.naver_price.text = "${item.price}원"
        view.naver_brand.text = item.brand

    }
}