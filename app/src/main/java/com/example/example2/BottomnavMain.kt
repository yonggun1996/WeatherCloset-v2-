package com.example.example2

import android.Manifest
import android.content.Context
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
    private val REQUEST_CODE_LOCATION = 2
    private var flag = false
    private var latitude = 0.0
    private var longitude = 0.0
    val TAG = "BottomnavMain"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottomnavigation)

        bottomnavigation.setOnNavigationItemSelectedListener(this)

        if(isLocationPermissionGranted()){
            getCurrentLocation()
        }

        storeFragment = BottomnavFragment1.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.bottomnav_framelayout, storeFragment).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.store ->{
                storeFragment = BottomnavFragment1.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.bottomnav_framelayout, storeFragment).commit()
            }
            R.id.now_search -> {
                now_searchFragment = BottomnavFragment2.newInstance()

                var bnf2 = BottomnavFragment2()//BottomnavFragment2 프래그먼트로 넘기기 위해 변수 선언
                var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
                bundle.putDouble("latitude",latitude)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다
                bundle.putDouble("longitude",longitude)

                bnf2.arguments = bundle

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.bottomnav_framelayout, bnf2)
                transaction.commit()
            }
            R.id.setting_search -> {
                setting_searchFragment = BottomnavFragment3.newInstance()

                var bnf3 = BottomnavFragment3()//BottomnavFragment2 프래그먼트로 넘기기 위해 변수 선언
                var bundle = Bundle()//프래그먼트는 Bundle로 데이터를 주고 받아야 해서 Bundle 객체 선언
                bundle.putDouble("latitude",latitude)//bundle로 데이터를 저장하는 방법, "latitude"는 키가 되고 기존에 구했던 위도를 저장한다 마찬가질 아래는 경도를 저정한다
                bundle.putDouble("longitude",longitude)

                bnf3.arguments = bundle

                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.bottomnav_framelayout, bnf3)
                transaction.commit()
            }
        }

        return true;
    }

    private fun isLocationPermissionGranted(): Boolean{
        val preference = getPreferences(Context.MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck",true)
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                //거부를 했을 때 사용자에게 왜 필요한지 이유를 설명
                Toast.makeText(this,"날씨를 알기 위해선 위치권한이 필요합니다.",Toast.LENGTH_SHORT).show()
            }else{
                if(isFirstCheck){
                    //앱을 처음 실행한 경우
                    preference.edit().putBoolean("isFirstPermissionCheck",false).apply()
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE_LOCATION)
                }else{
                    Toast.makeText(this,"위치가 파악이 안돼 날씨 데이터를 가져올 수 없습니다.",Toast.LENGTH_SHORT).show()
                }
            }
            return false
        }else{
            return true
        }

    }

    fun getCurrentLocation(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if(location !== null){//위치가 파악된 경우
                Log.d(TAG,"위치를 찾았습니다.")
                flag = true

                //위도와 경도를 저장하는 변수
                latitude = location.latitude
                longitude = location.longitude
                Log.d(TAG,"위도 : ${latitude}/ 경도 : ${longitude}")

            }else{
                Toast.makeText(this,"위치를 알 수 없습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == REQUEST_CODE_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getCurrentLocation()
            }else{
                Toast.makeText(this,"권한이 없어 해당 기능을 실행할 수 없습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

}


