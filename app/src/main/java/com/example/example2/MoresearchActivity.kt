package com.example.example2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.example2.Retrofit.NaverShoppingAPI
import com.example.example2.databinding.ActivityMoresearchBinding
import retrofit2.Call
import retrofit2.Response

class MoresearchActivity : AppCompatActivity() {

    //뷰 바인딩을 이용해 xml 코드 내의 레이아웃을 설정
    private lateinit var binding: ActivityMoresearchBinding
    private var moresearchList : ArrayList<NaverApiData> = ArrayList()
    private val TAG = "MoresearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moresearch)
        var closet = intent.getStringExtra("closet")

        binding = ActivityMoresearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.moresearchProgressbar.visibility = View.VISIBLE
        binding.moresearchTextview.text = "${closet} 확인"

        //Retrofit으로 parsing 하기 위한 인터페이스 create 호출
        val api = NaverShoppingAPI.create()

        //어노테이션으로 선언한 query에는 옷 종류가 display(검색 결과 수)는 30으로 전달
        api.get_ShoppingData(closet, 30).enqueue(object : retrofit2.Callback<ShoppingParse> {
            override fun onResponse(call: Call<ShoppingParse>, response: Response<ShoppingParse>) {
                var result = response.body()//레트로핏으로 받아온 결과
                var naverAPIArray = result!!.items

                for(i in naverAPIArray.indices){
                    var naverimgurl = naverAPIArray[i].image
                    var naverproductname = Html.fromHtml(naverAPIArray[i].title, Html.FROM_HTML_MODE_LEGACY).toString() //html 태그를 없애는 코드
                    var naverprice = naverAPIArray[i].lprice
                    var naverbrand = naverAPIArray[i].brand
                    var naverlink = naverAPIArray[i].link

                    moresearchList.add(NaverApiData(naverproductname, naverprice, naverbrand, naverimgurl, naverlink))
                }

                val gridLayoutManager = GridLayoutManager(applicationContext, 2)
                val adapter = MoresearchAdapter(moresearchList)
                binding.moresearchRecyclerview.adapter = adapter
                binding.moresearchRecyclerview.layoutManager = gridLayoutManager

            }

            override fun onFailure(call: Call<ShoppingParse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t}")
                TODO("Not yet implemented")
            }

        })

        binding.moresearchProgressbar.visibility = View.INVISIBLE

    }

}