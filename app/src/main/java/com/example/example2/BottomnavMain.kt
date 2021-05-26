package com.example.example2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.replace
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_bottomnavigation.*

class BottomnavMain : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener{

    private lateinit var storeFragment : BottomnavFragment1
    private lateinit var now_searchFragment : BottomnavFragment2
    private lateinit var setting_searchFragment : BottomnavFragment3
    private lateinit var confilm_weatherFragment : BottomnavFragment3_1
    private val REQUEST_CODE_LOCATION = 2
    private var flag = false
    private var latitude = 0.0
    private var longitude = 0.0
    private var now_item = 0
    var fragment3_date_time_List : ArrayList<Long> = ArrayList<Long>()
    val TAG = "BottomnavMain"
    var bnf2 = BottomnavFragment2()//BottomnavFragment2 프래그먼트로 넘기기 위해 변수 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        /*main에서 날씨에 대한 정보를 받아온 후 네비게이션 바를 누르면 그에 따른 화면을 보여주도록 수정 예정*/
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnavigation)

        //스플래시에서 넘긴 위도와 경도를 받는다
        val i = intent
        latitude = i.extras?.getDouble("latitude",0.0)!!//인텐트로 위도 얻어옴
        longitude = i.extras?.getDouble("longitude",0.0)!!//인텐트로 경도 얻어옴
        println("스플래시에서 넘겨받음 / 위도 : ${latitude} / 경도 : ${longitude}")

        bottomnavigation.setOnNavigationItemSelectedListener(this)

        storeFragment = BottomnavFragment1.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.bottomnav_framelayout, storeFragment).commit()
    }

    fun viewConfilmWeather(list : ArrayList<Long>){//설정한 날씨를 확인하게끔 해주는 메서드 , Activity에서 요청을 해서 띄우게끔 한다
        var bnf3_1 = BottomnavFragment3_1()//BottomnavFragment2 프래그먼트로 넘기기 위해 변수 선언
        var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
        bundle.putDouble("latitude",latitude)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다
        bundle.putDouble("longitude",longitude)
        fragment3_date_time_List = list
        Log.d(TAG,"${fragment3_date_time_List.get(0)}")
        //bundle.putParcelableArrayList("datelist",fragment3_date_time_List)
        bundle.putSerializable("datelist",fragment3_date_time_List)//프래그먼트에서 리스트르 옮기기 위해 사용된 메소드

        bnf3_1.arguments = bundle
        confilm_weatherFragment = BottomnavFragment3_1.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.bottomnav_framelayout, bnf3_1).commit()//fragment3_1으로 교체
        now_item = 4
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if(bnf2.settingimage){//Bottomnavigation2에서 이미지를 설정했다면 다른 프래그먼트로 이동하게끔 설정
            when(item.itemId){
                R.id.store ->{
                    storeFragment = BottomnavFragment1.newInstance()
                    supportFragmentManager.beginTransaction().replace(R.id.bottomnav_framelayout, storeFragment).commit()
                }
                R.id.now_search -> {
                    now_searchFragment = BottomnavFragment2.newInstance()

                    var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
                    bundle.putDouble("latitude",latitude)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다
                    bundle.putDouble("longitude",longitude)

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
        }else{//이미지 설정이 안된경우
            Toast.makeText(this,"이미지 로딩중입니다...",Toast.LENGTH_SHORT).show()
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


