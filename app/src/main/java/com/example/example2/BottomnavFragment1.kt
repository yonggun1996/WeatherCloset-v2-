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
            Log.d("Bottomnav1","받아온 결과 -> 위도 : ${latitude} / 경도 : ${longitude}")
            this.latitude = latitude
            this.longitude = longitude
        }


    }

    //뷰가 생성됐을 때 화면과 서로 연결
    //프래그먼트와 레이아웃을 연결시킨다
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        weather_jsonparse()
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
            }
        })
    }

}