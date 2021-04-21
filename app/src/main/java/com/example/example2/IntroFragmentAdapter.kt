package com.example.example2

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

//viewpager2의 새로운 페이지를 보여주는 클래스
class IntroFragmentAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 5
    }

    override fun createFragment(position: Int): Fragment {
        //FragmentActivity에 연결이 됐을 때
        //각 페이지마다 설정한 클래스를 반환해 각각의 내용을 보여준다
        if(position == 0){
            return Intro1Fragment()
        }else if(position == 1){
            return Intro2Fragment()
        }else if(position == 2){
            return Intro3Fragment()
        }else if(position == 3){
            return Intro4Fragment()
        }else{
            return Intro5Fragment()
        }
    }


}