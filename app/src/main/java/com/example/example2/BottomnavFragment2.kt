package com.example.example2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.util.Log
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_now_weather.*
import kotlinx.android.synthetic.main.activity_now_weather.text_coat
import kotlinx.android.synthetic.main.activity_now_weather.text_other
import kotlinx.android.synthetic.main.activity_now_weather.text_pants
import kotlinx.android.synthetic.main.activity_now_weather.text_shirt
import java.lang.StringBuilder

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomnavFragment2.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomnavFragment2 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    lateinit var requestQueue : RequestQueue
    private val TAG = "BottomnavFragment2"
    private var flag_temp = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //앞서 설정했던 위도와 경도를 Argument를 확인해 받아옵니다.
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            //번들로 넘겼던 객체를 가져오는 방법
            var latitude = it.getDouble("latitude")
            var longitude = it.getDouble("longitude")
            Log.d("Bottomnav2","받아온 결과 -> 위도 : ${latitude} / 경도 : ${longitude}")

            this.latitude = latitude
            this.longitude = longitude
        }
    }

    override fun onCreateView(//우선저그로 xml을 띄워준다
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //위치 권한 설정

        Log.d("BottomnavFragment2", "위도 : ${latitude}/ 경도 : ${longitude}")
        return inflater.inflate(R.layout.fragment_bottomnav2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {//ui에 관한 작업을 하는 코드
        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&exclude=24,daily=2&appid=APPKEY&units=metric"
        requestQueue = Volley.newRequestQueue(context);

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

            Response.Listener { response ->
                val jsonObject = response.getJSONObject("current")//current object를 불러온다
                val temp = jsonObject["temp"].toString()//현재 기온
                val feels_like = jsonObject["feels_like"].toString()//체감기온

                var current_weather_Array = jsonObject.getJSONArray("weather")//current object에 있는 weather 배열을 가져온다
                var current_weather_Object = current_weather_Array.getJSONObject(0)//weather배열의 id를 가져오는 코드
                var icon_code = current_weather_Object.getString("icon")//현재 날씨에 대한 이미지 아이콘 코드

                set_image(icon_code)//이미지 설정
                now_temp_tv.setText("현재온도 : ${Math.round(temp.toDouble())}")//현재 온도 설정
                feels_like_tv.setText("체감온도 : ${Math.round(feels_like.toDouble())}")//체감 온도 설정

                val now_weather_str = set_nowWeather(icon_code)
                now_weather.setText(now_weather_str)

                flag_temp = Math.round(temp.toDouble()).toInt()

                set_outer()
                set_shirt()
                set_pants()
                set_other()
            },
            Response.ErrorListener {
                Toast.makeText(context,"데이터를 호출하는데 실패했습니다.",Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonObjectRequest)
        super.onActivityCreated(savedInstanceState)
    }

    companion object {
        fun newInstance() : BottomnavFragment2{
            return BottomnavFragment2()
        }

    }

    /*private fun jsonparse(){
        Log.d("BottomnavFragment2", "json 호출")
        val url = "https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&exclude=24,daily=2&appid=7cfe1a321806b8117aab0b650a81555c&units=metric"
        requestQueue = Volley.newRequestQueue(context);

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

            Response.Listener { response ->
                val jsonObject = response.getJSONObject("current")//current object를 불러온다
                val temp = jsonObject["temp"].toString()//현재 기온
                val feels_like = jsonObject["feels_like"].toString()//체감기온

                var current_weather_Array = jsonObject.getJSONArray("weather")//current object에 있는 weather 배열을 가져온다
                var current_weather_Object = current_weather_Array.getJSONObject(0)//weather배열의 id를 가져오는 코드
                var icon_code = current_weather_Object.getString("icon")//현재 날씨에 대한 이미지 아이콘 코드

                set_image(icon_code)//이미지 설정
                now_temp_tv.setText("현재온도 : ${Math.round(temp.toDouble())}")//현재 온도 설정
                feels_like_tv.setText("체감온도 : ${Math.round(feels_like.toDouble())}")//체감 온도 설정

                val now_weather_str = set_nowWeather(icon_code)
                now_weather.setText(now_weather_str)

                flag_temp = Math.round(temp.toDouble()).toInt()

                set_outer()
                set_shirt()
                set_pants()
                set_other()
            },
            Response.ErrorListener {
                Toast.makeText(context,"데이터를 호출하는데 실패했습니다.",Toast.LENGTH_SHORT).show()
            }

        )
    }*/

    private fun set_image(icon_code : String){//current_weather_icon의 이미지를 설정하는 메소드
        val url = "http://openweathermap.org/img/wn/${icon_code}@2x.png"
        Glide.with(this)
            .load(url)
            .into(current_weather_icon)
    }

    private fun set_nowWeather(icon_code : String) : String{//아이콘 코드에 따라 현재 날씨를 텍슽트로 표현하는 메소드
        var weather = ""
        when (icon_code){
            "01d","01n" -> weather = "맑음"
            "02d","02n" -> weather = "구름 조금"
            "03d","03n" -> weather = "구름 낀"
            "04d","04n" -> weather = "구름 많음"
            "09d","09n" -> weather = "가끔 비"
            "10d","10n" -> weather = "비"
            "11d","11n" -> weather = "천둥번개"
            "13d","13n" -> weather = "눈"
            "50d","50n" -> weather = "안개"
        }
        return weather
    }

    //외투를 설정하는 함수
    private fun set_outer(){
        var outer_str =""
        val outer_stringbuilder = StringBuilder(outer_str)
        var word = true

        if(flag_temp <= 4){
            outer_stringbuilder.append("패딩, ")
            outer_stringbuilder.append("두꺼운 코트, ")
            word = false
        }

        if(flag_temp >= 5 && flag_temp <= 8){
            outer_stringbuilder.append("가죽자켓, ")
            outer_stringbuilder.append("코트, ")
            word = false
        }

        if(flag_temp >= 9 && flag_temp <= 16){
            outer_stringbuilder.append("자켓, ")
            outer_stringbuilder.append("야상, ")
            word = true
        }

        if(flag_temp >= 9 && flag_temp <= 14){
            outer_stringbuilder.append("트랜치코트, ")
            word = false
        }

        if(flag_temp >= 12 && flag_temp <= 19){
            outer_stringbuilder.append("가디건, ")
            word = true
        }

        if(flag_temp >= 18 && flag_temp <= 22){
            outer_stringbuilder.append("얇은 가디건, ")
            word = true
        }

        if(flag_temp >= 23){
            outer_stringbuilder.append("외투를 입지 않아도 되는 날씨 입니다.")
        }else{
            outer_stringbuilder.delete(outer_stringbuilder.length - 2, outer_stringbuilder.length)

            Log.d(TAG,"result : ${outer_stringbuilder}")

            if(word){
                outer_stringbuilder.append("을 입으시는걸 추천합니다.")
            }else{
                outer_stringbuilder.append("를 입으시는걸 추천합니다.")
            }
        }

        text_coat.setText(outer_stringbuilder)
    }

    //상의를 설정하는 함수
    private fun set_shirt(){
        var shirt = ""
        val shirt_stringbuilder = StringBuilder(shirt)
        var word = true

        if(flag_temp <= 4){
            shirt_stringbuilder.append("기모티셔츠, ")
            shirt_stringbuilder.append("두꺼운 티셔츠, ")
            word = false
        }

        if(flag_temp <= 13){
            shirt_stringbuilder.append("니트, ")
            word = false
        }

        if(flag_temp <= 19){
            shirt_stringbuilder.append("맨투맨, ")
            word = true
        }

        if(flag_temp >= 18 && flag_temp <= 22){
            shirt_stringbuilder.append("긴팔, ")
            shirt_stringbuilder.append("긴 셔츠, ")
            word = false
        }

        if (flag_temp >= 14 && flag_temp <= 19){
            shirt_stringbuilder.append("얇은 니트, ")
            word = false
        }

        if(flag_temp >= 23){
            shirt_stringbuilder.append("반팔, ")
            shirt_stringbuilder.append("린넨셔츠, ")
            word = false
        }

        if(flag_temp >= 28){
            shirt_stringbuilder.append("민소매, ")
            /*if(sex_code == 2){//추후에 사용자가 성별을 선택했을 때 주석을 풀기로
                shirt_stringbuilder.append("원피스, ")
            }*/
            word = false
        }

        shirt_stringbuilder.delete(shirt_stringbuilder.length - 2, shirt_stringbuilder.length)

        if(word){
            shirt_stringbuilder.append("을 입으시는걸 추천합니다.")
        }else{
            shirt_stringbuilder.append("를 입으시는걸 추천합니다.")
        }

        text_shirt.setText(shirt_stringbuilder)
        Log.d(TAG,"result : ${shirt_stringbuilder}")
    }

    //하의를 설정하는 함수
    private fun set_pants(){
        var pants = ""
        val pants_stringbuilder = StringBuilder(pants)
        var word = true

        if(flag_temp <= 4){
            pants_stringbuilder.append("기모바지, ")
            /*if(sex_code == 2){
                pants_stringbuilder.append("기모스타킹, ")
            }*/
            word = false
        }

        if(flag_temp <= 8){
            pants_stringbuilder.append("재질이 두꺼운 바지, ")
            word = false
        }

        if(flag_temp >= 9 && flag_temp <= 22){
            pants_stringbuilder.append("청바지, ")
            word = false
        }

        if(flag_temp >= 12 && flag_temp <= 27){
            pants_stringbuilder.append("면바지, ")
            word = false
        }

        if(flag_temp >= 25){
            pants_stringbuilder.append("반바지, ")
            /*if(sex_code == 2){
                pants_stringbuilder.append("치마, ")
            }*/
            word = false
        }

        pants_stringbuilder.delete(pants_stringbuilder.length - 2, pants_stringbuilder.length)

        if(word){
            pants_stringbuilder.append("을 입으시는걸 추천합니다.")
        }else{
            pants_stringbuilder.append("를 입으시는걸 추천합니다.")
        }

        text_pants.setText(pants_stringbuilder)
        Log.d(TAG,"result : ${pants_stringbuilder}")
    }

    //기타사항을 정의해주는 코드
    private fun set_other(){
        var other_str = ""

        if(flag_temp <= -1){
            other_str = "많이 춥습니다. 목도리, 장갑, 귀마개, 핫팩, 발열내의등 방한도구를 챙기시는걸 추천합니다."
        }else if(flag_temp >= 0 && flag_temp <= 4){
            other_str = "추위를 느낄수도 있습니다. 핫팩을 챙기거나 발열내의를 입으시는걸 추천합니다."
        }else if(flag_temp >= 26 && flag_temp <= 30){
            other_str = "더위를 느낄수도 있습니다. 꽉 끼는옷은 추천하지 않습니다."
        }else if(flag_temp >= 31){
            other_str = "많이 덥습니다. 통풍이 잘 되는 옷을 추천합니다."
        }
        text_other.setText(other_str)
    }

}