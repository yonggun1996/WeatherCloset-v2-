package com.example.example2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.example2.BottomNavigationFragment1.NaverApiData
import com.example.example2.BottomNavigationFragment1.ShoppingParse
import com.example.example2.Retrofit.NaverShoppingAPI
import com.example.example2.databinding.ActivityMoresearchBinding
import kotlinx.android.synthetic.main.activity_moresearch.view.*
import retrofit2.Call
import retrofit2.Response

class MoresearchActivity : AppCompatActivity() {

    //뷰 바인딩을 이용해 xml 코드 내의 레이아웃을 설정
    private lateinit var binding: ActivityMoresearchBinding
    private var moresearchList : ArrayList<NaverApiData> = ArrayList()//리사이클러 뷰를 보여주기 위한 리스트
    private val TAG = "MoresearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moresearch)
        var closet = intent.getStringExtra("closet")

        binding = ActivityMoresearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        view.moresearch_progressbar.visibility = View.VISIBLE
        //binding.moresearchProgressbar.visibility = View.VISIBLE//프로그레스바 활성화
        binding.moresearchTextview.text = "${closet} 확인"

        //Retrofit으로 parsing 하기 위한 인터페이스 create 호출
        val api = NaverShoppingAPI.create()

        //어노테이션으로 선언한 query에는 옷 종류가 display(검색 결과 수)는 30으로 전달
        api.get_ShoppingData(closet, 30).enqueue(object : retrofit2.Callback<ShoppingParse> {
            override fun onResponse(call: Call<ShoppingParse>, response: Response<ShoppingParse>) {
                val result = response.body()//레트로핏으로 받아온 결과
                Log.d(TAG, "Retrofit 호출한 결과 : ${result}")
                val naverAPIArray = result!!.items

                for (i in naverAPIArray.indices) {
                    val naverimgurl = naverAPIArray[i].image//이미지 url
                    val naverproductname =
                        Html.fromHtml(naverAPIArray[i].title, Html.FROM_HTML_MODE_LEGACY)
                            .toString() //html 태그를 없애는 코드
                    val naverprice = naverAPIArray[i].lprice//가격
                    val naverbrand = naverAPIArray[i].brand//브랜드명
                    val naverlink = naverAPIArray[i].link//해당 상품을 판매하는 링크

                    moresearchList.add(
                        NaverApiData(
                            naverproductname,
                            naverprice,
                            naverbrand,
                            naverimgurl,
                            naverlink
                        )
                    )
                }

                //리사이클러 뷰를 그리드 레이아웃으로 표현하는 코드
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

        //binding.moresearchProgressbar.visibility = View.INVISIBLE//프로그레스바 비활성화
        view.moresearch_progressbar.visibility = View.INVISIBLE

    }

}