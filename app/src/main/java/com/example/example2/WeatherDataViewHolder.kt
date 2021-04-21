package com.example.example2

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_set_weathers.view.*
import kotlinx.android.synthetic.main.activity_weather.*

//RecyclerView 어댑터에 사용할 뷰홀더
class WeatherDataViewHolder(v : View) : RecyclerView.ViewHolder(v) {

    var view : View = v//activity_set_weather xml파일의 이미지와 텍스트뷰를 설정하기위해 view를 끌어온다

    fun bind(item: WeatherData){

        //이미지 설정
        val url = "http://openweathermap.org/img/wn/${item.weather_img_code}@2x.png"
        Glide.with(view.context)
            .load(url)
            .into(view.setting_image_iv)

        view.setting_date_tv.text = item.date//날짜 설정
        view.setting_weather_tv.text = "날씨 : " + item.weather_text//날씨 설정
        view.setting_temp_tv.text = "기온 : " + item.temp.toString()//온도 설정
    }

}