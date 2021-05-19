package com.example.example2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_bottomnav1.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BottomnavFragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var latitude = 0.0
    private var longitude = 0.0
    private lateinit var fragment1Context: Context
    private lateinit var fragment1Activity: Activity
    lateinit var closetList : List<String>

    //프래그먼트를 안고있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        fragment1Context = context

        if(context is Activity){
            fragment1Activity = context as Activity
        }
        super.onAttach(context)
    }

    //메모리에 올라갔을 때
    //OnCreate() : 프래금먼트가 액티비티에 호출을 받아 생성되는 시점에 호출되는 메서드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Bottomnav1", "스토어의 onCreate()")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            var latitude = it.getDouble("latitude")
            var longitude = it.getDouble("longitude")
            this.latitude = latitude
            this.longitude = longitude

            Log.d("Bottomnav1","받아온 결과 -> 위도 : ${this.latitude} / 경도 : ${this.longitude}")
        }


    }

    //뷰가 생성됐을 때 화면과 서로 연결
    //프래그먼트와 레이아웃을 연결시킨다
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        if(this.latitude != 0.0 && this.longitude != 0.0){//위도와 경도가 받아들어져 왔다면 json 데이터를 읽어들인다
            weather_jsonparse()
        }
        return inflater.inflate(R.layout.fragment_bottomnav1, container, false)
    }

    companion object {
        fun newInstance() : BottomnavFragment1{//프래그먼트 자기자신을 가져오는 함수
            return BottomnavFragment1()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun weather_jsonparse(){//날씨 데이터를 파싱하는 메소드
        var weather_url = "https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&exclude=24,daily=2&appid=APPKEY&units=metric"
        val client = OkHttpClient()
        val request = Request.Builder().url(weather_url).build()

        client.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                Toast.makeText(context, "데이터를 불러들이는데 실패했습니다.",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                var str_responce = response?.body?.string()
                println("날씨 데이터 얻어오기 :" + str_responce)
                var weatherParse = Gson().fromJson<WeatherParse>(str_responce,WeatherParse::class.java)
                var now_Temp = Math.round(weatherParse.current!!.temp)//OKHTTP로 찾아낸 현재 기온
                println("현재 기온 : ${now_Temp.toInt()}")

                setClosetList(now_Temp.toInt())
            }
        })
    }

    private fun setClosetList(temp : Int){
        if(temp <= 4){
            closetList = listOf("패딩","두꺼운 코트","기모티셔츠","두꺼운니트","니트","맨투맨","기모바지")
        }else if(temp in 5..8){
            closetList = listOf("코트","가죽자켓","니트","맨투맨","청바지","기모바지")
        }else if(temp in 9..11){
            closetList = listOf("자켓","야상","트랜치코트","니트","맨투맨","청바지")
        }else if(temp in 12..14){
            closetList = listOf("자켓","야상","트랜치코트","가디건","니트","맨투맨","청바지","면바지")
        }else if(temp in 15..16){
            closetList = listOf("자켓","야상","가디건","니트","맨투맨","청바지","면바지")
        }else if(temp == 17){
            closetList = listOf("가디건","맨투맨","얇은니트","청바지","면바지")
        }else if(temp in 18..19){
            closetList = listOf("가디건","얇은 가디건","맨투맨","긴팔","긴 셔츠","얇은니트","청바지","면바지")
        }else if(temp in 20..22){
            closetList = listOf("얇은 가디건","긴팔","긴 셔츠","청바지","면바지")
        }else if(temp in 23..27){
            closetList = listOf("반팔","반 셔츠","린넨셔츠","면바지")
        }else if(temp >= 28){
            closetList = listOf("민소매","반팔","반 셔츠","린넨셔츠","반바지")
        }

        for(str in closetList){
            println("설정된 리스트의 값 : ${str}")
        }
    }

}