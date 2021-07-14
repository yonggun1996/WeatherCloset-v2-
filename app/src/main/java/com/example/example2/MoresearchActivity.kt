package com.example.example2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_moresearch.*

class MoresearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moresearch)

        var closet = intent.getStringExtra("closet")
        test_tv.text = closet
    }
}