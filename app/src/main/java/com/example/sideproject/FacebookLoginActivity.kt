package com.example.sideproject

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import java.security.MessageDigest

class FacebookLoginActivity : AppCompatActivity(){
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_login)

        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
            val signatures = info.signingInfo.apkContentsSigners
            val md = MessageDigest.getInstance("SHA")
            for(signature in signatures){
                val md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val key = String(Base64.encode(md.digest(),0))
                Log.d("Hash key:","!!!!!!!$key!!!!!!!")
            }
        }catch (e : Exception){
            Log.e("nae not found",e.toString())
        }
    }
}