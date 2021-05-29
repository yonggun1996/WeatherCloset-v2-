package com.example.example2

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices

class SplashActivity: AppCompatActivity() {

    private val REQUEST_CODE_LOCATION = 2
    var latitude : Double = 0.0
    var longitude : Double = 0.0
    var flag = false
    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//세로 고정

        val actionBar = supportActionBar
        actionBar?.hide()

        if(isLocationPermissionGranted()){
            getCurrentLocation()
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
            if(location !== null){//위치가 파악된 경우 BottomnavMain으로 넘긴다
                Log.d(TAG,"위치를 찾았습니다.")
                flag = true
                latitude = location.latitude
                longitude = location.longitude

                println("스플래시에서 얻어온 위치")
                println("위도 : ${latitude} / 경도 : ${longitude} / 위치를 받아왔는가? ${flag}")

                val nextIntent = Intent(this, BottomnavMain::class.java)
                nextIntent.putExtra("latitude",latitude)
                nextIntent.putExtra("longitude",longitude)
                startActivity(nextIntent)
            }else{
                Toast.makeText(this,"위치를 알 수 없습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }
}

