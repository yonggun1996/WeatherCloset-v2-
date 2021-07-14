package com.example.example2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bottomnavigation.*
import okhttp3.*
import java.io.IOException
import java.sql.DriverManager.println
import java.util.*

class BottomnavMain : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    private lateinit var storeFragment : BottomnavFragment1
    private lateinit var now_searchFragment : BottomnavFragment2
    private lateinit var setting_searchFragment : BottomnavFragment3
    private lateinit var confilm_weatherFragment : BottomnavFragment3_1
    private var latitude = 0.0
    private var longitude = 0.0
    private var now_item = 0
    var getjson = false
    private var parseData_String = ""
    var fragment3_date_time_List : ArrayList<Long> = ArrayList<Long>()
    var now_temp = 0.0//현재 기온
    var now_feellike = 0.0//현재 체감기온
    var now_imageCode = ""//이미지코드
    lateinit var hourly_array : Array<WeatherParse.Hourly>
    lateinit var hourly_weather_array : Array<WeatherParse.Hourly.Weather>
    val TAG = "BottomnavMain"
    var bnf1 = BottomnavFragment1()
    var bnf2 = BottomnavFragment2()//BottomnavFragment2 프래그먼트로 넘기기 위해 변수 선언
    //var progress : ProgressBar = findViewById(R.id.progressbar)

    override fun onCreate(savedInstanceState: Bundle?) {
        /*main에서 날씨에 대한 정보를 받아온 후 네비게이션 바를 누르면 그에 따른 화면을 보여주도록 수정 예정*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnavigation)

        //스플래시에서 넘긴 위도와 경도를 받는다
        val i = intent
        latitude = i.extras?.getDouble("latitude",0.0)!!//인텐트로 위도 얻어옴
        longitude = i.extras?.getDouble("longitude",0.0)!!//인텐트로 경도 얻어옴
        println("스플래시에서 넘겨받음 / 위도 : ${latitude} / 경도 : ${longitude}")

        //프로그래스 실행
        progressbar.visibility = View.VISIBLE
        //프로그래스가 실행되는 동안 프래그먼트를 연결할 수 없다
        //json 데이터 얻어오기(메서드를 만들고 응답받으면 메서드 내에서 다이얼로그 종료)
        //json 데이터 얻어오면 다이얼로그 종료
        //이 두가지가 동시에 실행되어야 한다

        weather_jsonparse()
        println("메인에서 json 파일을 얻어온 결과 : ${parseData_String}")

        bottomnavigation.setOnNavigationItemSelectedListener(this)

        //storeFragment = BottomnavFragment1.newInstance()
        //supportFragmentManager.beginTransaction().add(R.id.bottomnav_framelayout, storeFragment).commit()
    }

    private fun weather_jsonparse(){
        var weather_url = "https://api.openweathermap.org/data/2.5/onecall?lat=${latitude}&lon=${longitude}&exclude=24,daily=2&appid=&units=metric"
        val client = OkHttpClient()
        val request = Request.Builder().url(weather_url).build()

        client.newCall(request).enqueue(object  : Callback{
            override fun onFailure(call: Call, e: IOException) {
                //스레드가 달라서 그런지 context 불러들이는데 애를 먹는다 구글링 해보자
                //Toast.makeText(this,"데이터를 부르는 중입니다...",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call, response: Response) {
                parseData_String = response?.body?.string()!!
                println("데이터 얻어온 결과 : ${parseData_String}")
                getjson = true

                var weatherParse = Gson().fromJson(parseData_String,WeatherParse::class.java)
                now_temp = weatherParse.current!!.temp
                now_feellike = weatherParse.current!!.feels_like
                //now_imageCode = weatherParse.current!!.weather[3].toString() -> 잘못된 방식

                var now_weather_Array = weatherParse.current!!.weather//current 파라미터에서 리스트로 선언
                now_imageCode = now_weather_Array[0].icon//그 리스트를 가져오면 weatherList를 받아와 이미지 코드를 얻게 된다
                println("이미지 코드 : ${now_imageCode}")
                //progressbar.visibility = View.INVISIBLE//데이터를 다 받아왔으면 프로그레스바를 숨긴다

                hourly_array = weatherParse.hourly
                println("0번 인덱스 시간 : ${hourly_array[0].dt}")
                println("0번 인덱스 온도 : ${hourly_array[0].temp}")

                hourly_weather_array = hourly_array[0].weather
                println("0번 인덱스 날씨 아이콘 : ${hourly_weather_array[0].icon}")

                var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
                bundle.putDouble("now_Temp",now_temp)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다

                //데이터를 얻어오면 프래그먼트1에 데이터를 전달해 프래그먼트1 화면을 띄운다
                bnf1.arguments = bundle
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.bottomnav_framelayout, bnf1)
                transaction.commit()
                progressbar.visibility = View.INVISIBLE
            }

       })
    }

    fun viewConfilmWeather(list : ArrayList<Long>){//설정한 날씨를 확인하게끔 해주는 메서드 , Activity에서 요청을 해서 띄우게끔 한다
        var bnf3_1 = BottomnavFragment3_1()//BottomnavFragment3_1 프래그먼트로 넘기기 위해 변수 선언
        var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
        fragment3_date_time_List = list
        Log.d(TAG,"맨 첫 시간 : ${fragment3_date_time_List.get(0)}")
        //bundle.putParcelableArrayList("datelist",fragment3_date_time_List)
        bundle.putSerializable("datelist",fragment3_date_time_List)//프래그먼트에서 리스트르 옮기기 위해 사용된 메소드
        bundle.putSerializable("hourlyList", hourly_array)//시간별 온도의 데이터를 넘겨준다

        bnf3_1.arguments = bundle
        confilm_weatherFragment = BottomnavFragment3_1.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.bottomnav_framelayout, bnf3_1).commit()//fragment3_1으로 교체
        now_item = 4
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(getjson){//아직 json데이터를 읽지 못했다면 프래그먼트를 붙일 수 없다
            when(item.itemId){
                R.id.store ->{
                    storeFragment = BottomnavFragment1.newInstance()

                    var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
                    bundle.putDouble("now_Temp",now_temp)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다

                    bnf1.arguments = bundle
                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.bottomnav_framelayout, bnf1)
                    transaction.commit()
                }
                R.id.now_search -> {
                    now_searchFragment = BottomnavFragment2.newInstance()

                    var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
                    bundle.putDouble("now_Temp",now_temp)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다
                    bundle.putDouble("now_feellike",now_feellike)
                    bundle.putString("now_imageCode", now_imageCode)

                    bnf2.arguments = bundle

                    val transaction = supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.bottomnav_framelayout, bnf2)
                    transaction.commit()
                }
                R.id.setting_search -> {
                    if(now_item == 4){
                        viewConfilmWeather(fragment3_date_time_List)
                    }else{
                        setting_searchFragment = BottomnavFragment3.newInstance()

                        var bnf3 = BottomnavFragment3()//BottomnavFragment2 프래그먼트로 넘기기 위해 변수 선언
                        var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
                        bundle.putDouble("latitude",latitude)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다
                        bundle.putDouble("longitude",longitude)

                        bnf3.arguments = bundle

                        val transaction = supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.bottomnav_framelayout, bnf3)
                        transaction.commit()
                        now_item = 3
                    }

                }

            }
            return true
        }else{//json데이터를 아직 불러오지 않았을 경우
            Toast.makeText(this,"데이터를 부르는 중입니다...",Toast.LENGTH_SHORT).show()
            return false
        }
    }

    override fun onBackPressed() {
        if(now_item == 4){
            //BottomnavFragment3_1에 있는 requestQueue와 settingtime_list를 전부 지운다
            var bnf3_1 = BottomnavFragment3_1()
            bnf3_1.settingtime_list.clear()

            //3번으로 리플레이스
            var bnf3 = BottomnavFragment3()//BottomnavFragment3 프래그먼트로 넘기기 위해 변수 선언
            var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
            bundle.putDouble("latitude",latitude)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다
            bundle.putDouble("longitude",longitude)

            bnf3.arguments = bundle

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.bottomnav_framelayout, bnf3)
            transaction.commit()
            now_item = 3
        }else{
            //뒤로가기가 아닌 프로그램 종료
            moveTaskToBack(true)
            finishAndRemoveTask()
            android.os.Process.killProcess(android.os.Process.myPid())
        }
    }

}


