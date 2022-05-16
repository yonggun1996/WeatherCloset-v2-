package com.example.example2.BottomNavigationFragment3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.example2.R
import com.example.example2.WeatherDataViewHolder

class WeatherAdapter(val itemList : ArrayList<WeatherData>) : RecyclerView.Adapter<WeatherDataViewHolder>() {

    override fun getItemCount(): Int {
        //생성자로부터 받은 데이터의 갯수 측정
       return itemList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDataViewHolder {
        //어댑터에서 사용할 뷰홀더를 성정
        val inflatedView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_set_weathers,parent,false)

        return WeatherDataViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: WeatherDataViewHolder, position: Int) {
        //뷰홀더의 apply 함수를 이용해서 bind함수를 호출해서
        //각각의 데이터를 item_contacts를 사용하는 ViewHolder에 적용한다.
        val item = itemList[position]
        holder.apply {
            bind(item)
        }
    }
}