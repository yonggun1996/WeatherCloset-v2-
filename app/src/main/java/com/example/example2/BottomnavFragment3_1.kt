package com.example.example2

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_weather.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BottomnavFragment3_1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var latitude :Double? = null
    var longitude :Double? = null
    var settingtime_list : ArrayList<Long> = ArrayList<Long>()
    var sex_code = 0
    var unixtime1 = 0L
    var unixtime2 = 0L
    var unixtime3 = 0L
    var unixtime4 = 0L
    val temp_list = ArrayList<Int>()
    var flag_temp = 0 //기준이 되는 온도 설정
    var daily_cross_flag = false//일교차를 느끼는 온도인가 아닌가
    var hot_day = false
    var cold_day = false
    val setting_weather_list : ArrayList<WeatherData> = ArrayList<WeatherData>()
    var url : String = ""
    val TAG = "BottomnavFragment3"
    lateinit var requestQueue : RequestQueue

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            //번들로 넘겼던 객체를 가져오는 방법
            var latitude = it.getDouble("latitude")
            var longitude = it.getDouble("longitude")
            var timelist : ArrayList<Long> = it.getSerializable("datelist") as ArrayList<Long>
            Log.d("Bottomnav3_1","받아온 결과 -> 위도 : ${latitude} / 경도 : ${longitude}")

            this.latitude = latitude
            this.longitude = longitude
            this.settingtime_list = timelist
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottomnav_fragment3_1, container, false)
    }

    companion object {
        fun newInstance() : BottomnavFragment3_1{
            return BottomnavFragment3_1()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        url = "https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&exclude=24,daily=2&appid=APPEKY&units=metric"
        requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener { response ->
                val jsonArray = response.getJSONArray("hourly")

                unixtime1 = settingtime_list.get(0)
                unixtime2 = settingtime_list.get(1)
                Log.d(TAG,"유닉스타임1 : ${unixtime1}")

                //unixtime을 이용해 JSON 배열의 값을 읽어들인다
                val now_unixtime = jsonArray.getJSONObject(0).getString("dt").toLong()
                var start = (unixtime1 - now_unixtime) / 3600//시작시간
                var finish = (unixtime2 - now_unixtime) / 3600//끝시간

                //외출시간1을 입력받은대로 배열에 담는 과정
                var list_index = 0
                var temp_total = 0
                for (index in start .. finish){
                    var dt_date = jsonArray.getJSONObject(index.toInt()).getLong("dt")//설정시간의 unixtime
                    //var dt_feels_like = jsonArray.getJSONObject(index.toInt()).getDouble("feels_like")//설정시간의 체감온도
                    var dt_temp = jsonArray.getJSONObject(index.toInt()).getDouble("temp")//설정시간의 기온
                    var dt_weather = jsonArray.getJSONObject(index.toInt()).getJSONArray("weather")//설정시간의 Weather 배열을 불러오는 코드
                    var weather_array = dt_weather.getJSONObject(0)//Weather 배열을 읽어들이는 코드
                    var weather_icon = weather_array.getString("icon")//그 중 아이콘을 불러온다

                    temp_list.add(list_index,Math.round(dt_temp).toInt())

                    Log.d(TAG,"시간 : ${index} / 기온 : ${dt_temp}")

                    temp_total += Math.round(dt_temp).toInt()
                    list_index++
                    var setting_weather_txt = get_setting_weather(weather_icon)
                    var setting_date_txt = unixtoDate(dt_date)

                    //RecyclerView를 이용하기 위해 WeatherData 클래스에
                    //날짜, 날씨이미지, 날씨, 체감기온을 전달해 준다
                    Log.d(TAG,"날짜 : ${setting_date_txt}, 이미지코드 : ${weather_icon}, 날씨 : ${setting_weather_txt}, 기온 : ${Math.round(dt_temp).toInt()}")
                    setting_weather_list.add(WeatherData(setting_date_txt,weather_icon,setting_weather_txt,Math.round(dt_temp).toInt()))
                }//for

                //만약 외출시간2도 입력받았다면 그것도 배열에 담는다
                if(settingtime_list.size == 4){
                    unixtime3 = settingtime_list.get(2)
                    unixtime4 = settingtime_list.get(3)
                    start = (unixtime3 - now_unixtime) / 3600
                    finish = (unixtime4 - now_unixtime) / 3600

                    for (index in start .. finish){
                        var dt_date = jsonArray.getJSONObject(index.toInt()).getLong("dt")//입력한 퇴근(하교)시간
                        //var dt_feels_like = jsonArray.getJSONObject(index.toInt()).getDouble("feels_like")//입력한 시간의 체감온도
                        var dt_temp = jsonArray.getJSONObject(index.toInt()).getDouble("temp")//설정시간의 기온
                        var dt_weather = jsonArray.getJSONObject(index.toInt()).getJSONArray("weather")//설정시간의 Weather 배열을 불러오는 코드
                        var weather_array = dt_weather.getJSONObject(0)//Weather 배열을 설정하는 코드
                        var weather_icon = weather_array.getString("icon")//그 중 아이콘을 불러온다

                        temp_list.add(list_index,Math.round(dt_temp).toInt())

                        Log.d(TAG,"시간 : ${index} / 기온 : ${Math.round(dt_temp)}")

                        temp_total += Math.round(dt_temp).toInt()
                        list_index++

                        var setting_weather_txt = get_setting_weather(weather_icon)
                        var setting_date_txt = unixtoDate(dt_date)

                        //RecyclerView를 이용하기 위해 WeatherData 클래스에
                        //날짜, 날씨이미지, 날씨, 체감기온을 전달해 준다
                        Log.d(TAG,"날짜 : ${setting_date_txt}, 이미지코드 : ${weather_icon}, 날씨 : ${setting_weather_txt}, 기온 : ${Math.round(dt_temp).toInt()}")
                        setting_weather_list.add(WeatherData(setting_date_txt,weather_icon,setting_weather_txt,Math.round(dt_temp).toInt()))
                    }
                }

                set_getup_tv(temp_total, list_index)
                set_outer()
                set_shirt()
                set_pants()
                set_other()

                //RecyclerView를 가로로 설정하기위한 코드
                mRecyclerView.layoutManager = LinearLayoutManager(context).also { it.orientation = LinearLayoutManager.HORIZONTAL }

                val adapter = WeatherAdapter(setting_weather_list)
                mRecyclerView.adapter = adapter
            },
            Response.ErrorListener { error ->
                Toast.makeText(context,"데이터를 호출하는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        )
        requestQueue.add(jsonObjectRequest)
        super.onActivityCreated(savedInstanceState)
    }

    //기준온도를 정하는 함수
    private fun set_getup_tv(temp_total : Int, list_size : Int){
        temp_list.sort()

        var cold = 0//추운 온도는 얼마나 있는가
        var hot = 0//더운 온도는 얼마나 있는가

        //추운온도, 더운온도가 얼마나 있는지 나타내는 코드
        for(i in 0 .. list_size -1){
            if(temp_list[i] <= 9){
                cold++
            }else if(temp_list[i] >= 26){
                hot++
            }
        }//for

        Log.d(TAG, "총 온도 : ${temp_total} / 리스트 길이 : ${list_size} / 추운시간 : ${cold} / 더운시간 : ${hot}")
        //평균온도를 정하는 코드
        if(cold >= list_size / 2){//추운날이 절반 이상이면 최저온도
            flag_temp = temp_list.get(0)
            cold_day = true
        }else if(hot >= list_size / 2){//더운날이 절반 이상이면 최고온도
            flag_temp = temp_list.get(list_size - 1)
            hot_day = true
        }else{//둘 다 해당되지 않으면 평균온도
            var temp_double= temp_total.toDouble() / list_size.toDouble()
            flag_temp = Math.round(temp_double).toInt()
        }

        if((temp_list.get(0) < 23 && temp_list.get(list_size - 1) > 4) && temp_list.get(list_size - 1) - temp_list.get(0) >= 7){
            //일교차가 심하면 true
            daily_cross_flag = true
        }

        Log.d(TAG,"기준온도 : ${flag_temp} / 추운가? : ${cold_day} / 더운가? : ${hot_day}")
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
            other_str = "많이 춥습니다. 목도리, 장갑, 귀마개, 핫팩, 발열내의등 방한도구를 챙기시는걸 추천합니다.\n"
        }else if(flag_temp >= 0 && flag_temp <= 4){
            other_str = "추위를 느낄수도 있습니다. 핫팩을 챙기거나 발열내의를 입으시는걸 추천합니다.\n"
        }else if(flag_temp >= 26 && flag_temp <= 30){
            other_str = "더위를 느낄수도 있습니다. 꽉 끼는옷은 추천하지 않습니다.\n"

        }else if(flag_temp >= 31){
            other_str = "많이 덥습니다. 통풍이 잘 되는 옷을 추천합니다.\n"

        }

        //일교차가 느껴진다면
        if(daily_cross_flag){
            var max_temp = temp_list.get(temp_list.size - 1)
            var min_temp = temp_list.get(0)
            Log.d(TAG,"최고온도 : ${max_temp} / 최저온도 : ${min_temp}")
            Log.d(TAG,"추운가? : ${cold_day} / 더운가? : ${hot_day}")

            if(cold_day){
                var cold_day_closet = Outerwear_temperature(max_temp)
                Log.d(TAG,"추울 땐 ${cold_day_closet}")
                other_str += "\n일교차가 큰 날씨입니다. 더위를 잘 탄다면 ${cold_day_closet}를 입는걸 추천합니다.\n"
            }else if(hot_day){
                var hot_day_closet = Outerwear_temperature(min_temp)
                Log.d(TAG,"더울 땐 ${hot_day_closet}")
                other_str += "\n일교차가 큰 날씨입니다. 추위를 잘 탄다면 ${hot_day_closet}를 입는걸 추천합니다.\n"
            }else{
                var cold_day_closet = Outerwear_temperature(min_temp)
                var hot_day_closet = Outerwear_temperature(max_temp)
                Log.d(TAG,"추울 땐 ${cold_day_closet} 더울 땐 ${hot_day_closet}")
                other_str += "\n일교차가 큰 날씨입니다. 더위를 잘 탄다면 ${hot_day_closet} 추위를 잘 탄다면 ${cold_day_closet} 추천합니다."
            }
        }

        text_other.setText(other_str)
    }

    //아이콘에 대한 날씨를 반환해주는 코드
    private fun get_setting_weather(code : String) : String{
        var weather = ""
        when (code){
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

    //설정시간의 Date를 반환하는 함수
    private fun unixtoDate(unixtime : Long) : String{
        val sdf = SimpleDateFormat("MM월 dd일 HH시")
        var set_unix = unixtime * 1000
        val date = Date(set_unix)
        date.time
        var sdf_str = sdf.format(date)
        return sdf_str
    }

    private fun Outerwear_temperature(temp : Int) : String{
        var outer = ""
        var max_temp = temp_list.get(temp_list.size - 1)
        var min_temp = temp_list.get(0)
        if(cold_day){
            if(max_temp >= 5 && max_temp <= 8){
                outer = "코트를"
            }else if(max_temp >= 9 && max_temp <= 14){
                outer = "자켓을"
            }else if(max_temp >= 15 && max_temp <= 19){
                outer = "가디건을"
            }else if(max_temp >= 20 && max_temp <= 22){
                outer = "얇은 가디건을"
            }else{
                outer = "외투를 입지 않는것을"
            }
        }else if(hot_day){

            if(min_temp >= 5 && min_temp <= 8){
                outer = "코트를"
            }else if(min_temp >= 9 && min_temp <= 14){
                outer = "자켓을"
            }else if(min_temp >= 15 && min_temp <= 19){
                outer = "가디건을"
            }else if(min_temp >= 20 && min_temp <= 22){
                outer = "얇은 가디건을"
            }else{
                outer = "패딩을"
            }
        }else{
            if(temp <= 4){
                outer = "패딩을"
            } else if(temp >= 5 && temp <= 8){
                outer = "코트를"
            }else if(temp >= 9 && temp <= 14){
                outer = "자켓을"
            }else if(temp >= 15 && temp <= 19){
                outer = "가디건을"
            }else if(temp >= 20 && temp <= 22){
                outer = "얇은 가디건을"
            }else{
                outer = "외투를 입지 않는것을"
            }
        }

        return outer
    }


}