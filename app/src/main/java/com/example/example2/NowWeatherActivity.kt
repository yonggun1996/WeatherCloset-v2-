package com.example.example2

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_now_weather.*
import kotlinx.android.synthetic.main.activity_weather.text_coat
import kotlinx.android.synthetic.main.activity_weather.text_other
import kotlinx.android.synthetic.main.activity_weather.text_pants
import kotlinx.android.synthetic.main.activity_weather.text_shirt
import java.lang.StringBuilder

class NowWeatherActivity : AppCompatActivity() {

    var latitude :Double? = null
    var longitude :Double? = null
    var sex_code = 0
    var flag_temp = 0
    private val TAG = "WeatherActivity"
    lateinit var requestQueue : RequestQueue

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_now_weather)
        setTitle("현재 시간으로 확인")
        supportActionBar?.setLogo(R.drawable.appicon)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//세로 고정

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
        }

        Log.d(TAG, "넘어왔습니다.")
        val i = intent
        latitude = i.extras?.getDouble("latitude",0.0)//인텐트로 위도 얻어옴
        longitude = i.extras?.getDouble("longitude",0.0)//인텐트로 경도 얻어옴
        sex_code = i.getIntExtra("sex_code",0)

        jsonparse()

    }

    private fun jsonparse() {
        val url =
            "https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&exclude=24,daily=2&appid=APPKEY&units=metric"
        val requestQueue = Volley.newRequestQueue(application)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,

            Response.Listener { response ->
                val jsonObject = response.getJSONObject("current")
                val temp = jsonObject["temp"].toString()
                val feels_like = jsonObject["feels_like"].toString()

                Log.d(TAG,"기온 : ${temp} / 체감온도 : ${feels_like}")

                var current_weather_array = jsonObject.getJSONArray("weather")
                var current_weather_object = current_weather_array.getJSONObject(0)
                var icon_code = current_weather_object.getString("icon")
                Log.d(TAG,"아이콘 : ${icon_code}")

                set_image(icon_code)
                now_temp_tv.setText("현재온도 : ${Math.round(temp.toDouble())}")//현재 온도 설정
                feels_like_tv.setText("체감온도 : ${Math.round(feels_like.toDouble())}")//체감 온도 설정

                val nowweather_str = set_nowWeather(icon_code)
                now_weather.setText(nowweather_str)

                flag_temp = Math.round(temp.toDouble()).toInt()

                set_outer()
                set_shirt()
                set_pants()
                set_other()
            },
            Response.ErrorListener {
                Toast.makeText(this,"데이터를 호출하는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonObjectRequest)
    }

    private fun set_image(icon_code : String){
        val url = "http://openweathermap.org/img/wn/${icon_code}@2x.png"
        Glide.with(this)
            .load(url)
            .into(current_weather_icon)
    }

    private fun set_nowWeather(icon_code : String) : String{
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
            if(sex_code == 2){
                shirt_stringbuilder.append("원피스, ")
            }
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
            if(sex_code == 2){
                pants_stringbuilder.append("기모스타킹, ")
            }
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
            if(sex_code == 2){
                pants_stringbuilder.append("치마, ")
            }
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

    //back버튼을 눌렀을 때 동작
    override fun onBackPressed() {
        super.onBackPressed()
    }
}