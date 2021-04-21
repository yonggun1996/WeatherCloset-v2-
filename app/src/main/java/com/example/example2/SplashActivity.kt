package com.example.example2

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity: AppCompatActivity() {

    val SPLASH_TIME : Long = 1500
    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//세로 고정

        val actionBar = supportActionBar
        actionBar?.hide()

        val pref : SharedPreferences = getSharedPreferences("checkFirst", Activity.MODE_PRIVATE)
        var checkFirst = pref.getBoolean("checkFirst",false)

        if(!checkFirst){//처음 설치해서 실행할 경우 인트로 화면을 보여준다
            Handler().postDelayed({
                Log.d(TAG, "처음 실행합니다.${checkFirst}")
                startActivity(Intent(this, FragmentActivity::class.java))
                pref.edit().putBoolean("checkFirst",true).apply()//설치했고 열어봤다는걸 기록하기 위해 true로 초기화한다
                finish()
            },SPLASH_TIME)

        }else{
            Handler().postDelayed({//이미 앱을 실행시킨 이력이 있는 경우
                startActivity(Intent(this, BottomnavMain::class.java))
                finish()
            },SPLASH_TIME)
        }



    }
}

