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

        Handler().postDelayed({//이미 앱을 실행시킨 이력이 있는 경우
            startActivity(Intent(this, BottomnavMain::class.java))
            finish()
        },SPLASH_TIME)
    }
}

