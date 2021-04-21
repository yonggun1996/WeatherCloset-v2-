package com.example.example2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import kotlinx.android.synthetic.main.activity_fragment_main.*

class FragmentActivity : AppCompatActivity(){

    private val TAG = "FragmentActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment_main)
        Log.d(TAG, "intro page")

        //액션바 숨김
        val actionBar = supportActionBar
        actionBar?.hide()

        val viewPager2 : ViewPager2 = findViewById(R.id.intro_pager)
        val adapter_val = IntroFragmentAdapter(this)//Adapter 클래스와 연결하기 위한 상수
        intro_pager.adapter = adapter_val//activity_fragment_main에서 설정한 viewpager2를 어댑터와 연결한다


        next_button.setOnClickListener {
            if(viewPager2.currentItem == 4){//마침버튼을 누르면 main페이지로 이동
                startActivity(Intent(this, BottomnavMain::class.java))
                finish()
            }else{//버튼을 눌렀을 때 마지막 페이지가 아니면 다음 장으로 넘긴다
                viewPager2.currentItem = viewPager2.currentItem + 1
            }
        }

        //viewpager의 몇 번째 페이지인가를 알게 하는 코드
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if(position == 4){//마지막 페이지면 버튼이 마침 버튼으로 바뀐다
                    next_button.setText("마침")
                }else{//그렇지 않으면 다음 버튼이 된다
                    next_button.setText("다음")
                }
            }
        })

    }


}