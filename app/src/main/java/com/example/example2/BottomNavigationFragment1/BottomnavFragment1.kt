package com.example.example2.BottomNavigationFragment1

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.example2.R
import com.example.example2.Retrofit.NaverShoppingAPI
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_bottomnav1.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class BottomnavFragment1 : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var get_now_temp = 0.0
    private var int_now_temp = 0
    private lateinit var fragment1Context: Context
    private lateinit var fragment1Activity: Activity
    private val TAG = "BottomnavFragment1"

    //프래그먼트를 안고있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
        Log.d(TAG, "onAttach 메서드 호출")
        fragment1Context = context

        if(context is Activity){
            fragment1Activity = context as Activity
        }
        super.onAttach(context)
    }

    //메모리에 올라갔을 때
    //OnCreate() : 프래그먼트가 액티비티에 호출을 받아 생성되는 시점에 호출되는 메서드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate() 호출")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            this.get_now_temp = it.getDouble("now_Temp")
            int_now_temp = Math.round(get_now_temp).toInt()
            Log.d(TAG, "프래그먼트 1 현재온도 :  ${int_now_temp}")
        }


    }

    //뷰가 생성됐을 때 화면과 서로 연결
    //프래그먼트와 레이아웃을 연결시킨다
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView 호출 ")
        return inflater.inflate(R.layout.fragment_bottomnav1, container, false)
    }

    companion object {
        fun newInstance() : BottomnavFragment1{//프래그먼트 자기자신을 가져오는 함수
            return BottomnavFragment1()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d(TAG, "onActivityCreated 호출")
        fragment1_progressbar?.visibility = View.VISIBLE
        val closet_list = setClosetList()//해당 기온에 맞는 옷들을 담는 리스트를 구현하는 메서드 호출
        val setting_closetList = ArrayList<Fragment1OutData>()

        //레트로핏을 동기방식으로 호출 해야 리스트에 데이터가 closet_list의 길이 만큼 세팅된다.
        CoroutineScope(Dispatchers.Main).launch {//UI부분을 담당하는 코루틴
            val api = NaverShoppingAPI.create()
            lateinit var naverdatalist : ArrayList<NaverApiData>

            for(str in closet_list){
                naverdatalist = ArrayList()
                val closetJSON = CoroutineScope(Dispatchers.Default).launch {//네트워크단 코루틴
                    val closetData = api.get_ShoppingData(str).execute()//레트로핏을 동기방식으로 호출
                    val body = closetData.body()
                    val shopitemarray = body?.items

                    for(i in 0..9){
                        val title = Html.fromHtml(shopitemarray?.get(i)?.title, Html.FROM_HTML_MODE_LEGACY)
                            .toString() //html 태그를 없애는 코드
                        val price = shopitemarray?.get(i)?.lprice
                        val brand = shopitemarray?.get(i)?.brand
                        val image_url = shopitemarray?.get(i)?.image
                        val link = shopitemarray?.get(i)?.link

                        naverdatalist.add(NaverApiData(title, price!!, brand!!, image_url!!, link!!))
                    }
                }.join()//데이터를 호출할 때 까지 기다린다

                Log.d(TAG, "naverdatalist : ${naverdatalist.size}")
                setting_closetList.add(Fragment1OutData(str, naverdatalist))
            }

            //RecyclerView를 세팅하는 코드
            Log.d(TAG, "setting_closetList : ${setting_closetList.size}")
            fragment1_progressbar?.visibility = View.INVISIBLE
            val adapter = Fragment1OutAdapter(setting_closetList)
            now_temp_rv?.adapter = adapter
            //버튼을 누르면 여기서 화면이 전환되게끔
            now_temp_rv?.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }
        }

        super.onActivityCreated(savedInstanceState)
    }

    //현재 기온에 맞는 옷들을 담은 리스트를 반환하는 메서드
    private fun setClosetList() : List<String>{
        lateinit var closetList : List<String>
        when{
            int_now_temp <= 4 -> closetList = listOf("패딩","두꺼운 코트","기모티셔츠","두꺼운니트","니트","맨투맨","기모바지")
            int_now_temp in 5 .. 8 -> closetList = listOf("코트","가죽자켓","니트","맨투맨","청바지","기모바지")
            int_now_temp in 9 .. 11 -> closetList = listOf("자켓","야상","트랜치코트","니트","맨투맨","청바지")
            int_now_temp in 12 .. 14 -> closetList = listOf("자켓","야상","트랜치코트","가디건","니트","맨투맨","청바지","면바지")
            int_now_temp in 15 .. 16 -> closetList = listOf("자켓","야상","가디건","니트","맨투맨","청바지","면바지")
            int_now_temp == 17 -> closetList = listOf("가디건","맨투맨","얇은니트","청바지","면바지")
            int_now_temp in 18 .. 19 -> closetList = listOf("가디건","얇은 가디건","맨투맨","긴팔","긴 셔츠","얇은니트","청바지","면바지")
            int_now_temp in 20 .. 22 -> closetList = listOf("얇은 가디건","긴팔","긴 셔츠","청바지","면바지")
            int_now_temp in 23 .. 27 -> closetList = listOf("반팔","반 셔츠","린넨셔츠","면바지")
            int_now_temp >= 28 -> closetList = listOf("민소매","반팔","반 셔츠","린넨셔츠","반바지")
        }
        return closetList
    }
}