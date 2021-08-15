package com.example.example2

import android.app.Activity
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.example2.databinding.ActivityBottomnavigationBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_bottomnavigation.*
import kotlinx.android.synthetic.main.activity_inviewholder.*
import kotlinx.android.synthetic.main.activity_out1viewholder.*
import kotlinx.android.synthetic.main.fragment_bottomnav1.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
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
    private lateinit var closetList : List<String>
    private var setting_closetList : ArrayList<Fragment1OutData> = ArrayList()
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
        setClosetList()//해당 기온에 맞는 옷들을 담는 리스트를 구현하는 메서드 호출
        set_coroutine()//네이버 쇼핑 api를 파싱하는 메서드 호출

        // 이 프래그먼트가 액티비티에 떼어지면 기존에 가지고있던 옷 리스트의 데이터를 지운다
        // 더 보기 액티비티로 넘긴 후 다시 돌아오니 에러 발생 clear 하는 시점을 변경
        setting_closetList.clear()

        super.onActivityCreated(savedInstanceState)
    }

    //네이버 쇼핑 정보를 가져오기 위해 코루틴을 실행
    private fun set_coroutine(){
        CoroutineScope(Dispatchers.Main).launch {//UI 작업을 하는 Main Dispatchers
            for(str in closetList) {
                lateinit var naverapi_list : ArrayList<NaverApiData>//json 데이터를 파싱한 결과

                var closetJSON = CoroutineScope(Dispatchers.Default).launch {//네이버 쇼핑 API를 Parsing하는 Default Dispatchers
                    //서버단의 작업
                    naverapi_list = ArrayList<NaverApiData>(naverjsonparse(str))
                    //println("JSON Parsing${str} : ${closjon}")
                }.join()//JSON 데이터를 가져올 때 까지 대기한다

                //ui단의 작업
                //작은 rv 작업

                setting_closetList.add(Fragment1OutData(str, naverapi_list))

            }

            fragment1_progressbar?.visibility = View.INVISIBLE
            val adapter = Fragment1OutAdapter(setting_closetList)
            now_temp_rv?.adapter = adapter
            //버튼을 누르면 여기서 화면이 전환되게끔
            now_temp_rv?.layoutManager = LinearLayoutManager(context).also {
                it.orientation = LinearLayoutManager.VERTICAL
            }

        }

    }

    private fun setClosetList(){
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

    }

    //네이버 쇼핑 api의 json 데이터를 파싱하는 메서드
    //이 라인부터 233라인 까지
    //출처 : https://developers.naver.com/docs/serviceapi/search/shopping/shopping.md#%EC%87%BC%ED%95%91
    private fun naverjsonparse(keyword : String) : ArrayList<NaverApiData>{
        val clientId = "" //애플리케이션 클라이언트 아이디값"

        val clientSecret = "" //애플리케이션 클라이언트 시크릿값"


        var text: String? = null
        text = try {
            URLEncoder.encode(keyword, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("검색어 인코딩 실패", e)
        }


        val apiURL =
            "https://openapi.naver.com/v1/search/shop?query=$text" // json 결과

        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과


        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
        val requestHeaders: MutableMap<String, String> = HashMap()
        requestHeaders["X-Naver-Client-Id"] = clientId
        requestHeaders["X-Naver-Client-Secret"] = clientSecret
        val responseBody: String? = get(apiURL, requestHeaders)

        var result = Html.fromHtml(responseBody, Html.FROM_HTML_MODE_LEGACY).toString()
        println("JSON Parsing${ keyword} : ${result}")

        var shoppingparse = Gson().fromJson(result, ShoppingParse::class.java)
        var shopitemarray = shoppingparse.items

        //검색 결과 10개를 토대로 작은 RecyclerView를 만든다
        //indices : 배열의 인덱스 범위를 반환
        var naverdatalist = ArrayList<NaverApiData>()
        for(i in 0..9){
            //변수를 설정한 후 RecyclerView를 만들기
            var title = shopitemarray[i].title
            var price = shopitemarray[i].lprice
            var brand = shopitemarray[i].brand
            var image_url = shopitemarray[i].image
            var link = shopitemarray[i].link

            naverdatalist.add(NaverApiData(title, price, brand, image_url, link))
        }

        return naverdatalist
    }

    private operator fun get(
        apiUrl: String,
        requestHeaders: Map<String, String>
    ): String? {
        val con: HttpURLConnection = connect(apiUrl)
        return try {
            con.requestMethod = "GET"
            for ((key, value) in requestHeaders) {
                con.setRequestProperty(key, value)
            }
            val responseCode: Int = con.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                readBody(con.inputStream)
            } else { // 에러 발생
                readBody(con.errorStream)
            }
        } catch (e: IOException) {
            throw java.lang.RuntimeException("API 요청과 응답 실패", e)
        } finally {
            con.disconnect()
        }
    }


    private fun connect(apiUrl: String): HttpURLConnection {
        return try {
            val url = URL(apiUrl)
            url.openConnection() as HttpURLConnection
        } catch (e: MalformedURLException) {
            throw java.lang.RuntimeException("API URL이 잘못되었습니다. : $apiUrl", e)
        } catch (e: IOException) {
            throw java.lang.RuntimeException("연결이 실패했습니다. : $apiUrl", e)
        }
    }


    private fun readBody(body: InputStream): String? {
        val streamReader = InputStreamReader(body)
        try {
            BufferedReader(streamReader).use { lineReader ->
                val responseBody = StringBuilder()
                responseBody.append("{")
                var line: String? = lineReader.readLine()
                while (lineReader.readLine().also { line = it } != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw java.lang.RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }
}