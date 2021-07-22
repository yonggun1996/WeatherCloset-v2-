package com.example.example2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.ActionBar
import kotlinx.android.synthetic.main.activity_web_view.*

class WebViewActivity : AppCompatActivity() {
    private val TAG = "WebViewActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val url = intent.getStringExtra("URL")
        Log.d(TAG, "URL : ${url} ")

        var actionBar : ActionBar?
        actionBar = supportActionBar
        actionBar?.hide()

        webview.settings.javaScriptEnabled = true //웹 페이지 내의 javascript를 허용

        //웹뷰에 새창이 뜨지 않도록 한다다
        webview.webViewClient = WebViewClient()
        webview.webChromeClient = WebChromeClient()
        webview.loadUrl(url!!)//링크 주소를 로드함
    }
}