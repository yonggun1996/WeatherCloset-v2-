package com.example.example2

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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
    lateinit var closetList : List<String>
    var setting_closetList : ArrayList<Fragment1OutData> = ArrayList<Fragment1OutData>()

    //프래그먼트를 안고있는 액티비티에 붙었을 때
    override fun onAttach(context: Context) {
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
        Log.d("Bottomnav1", "스토어의 onCreate()")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

            this.get_now_temp = it.getDouble("now_Temp")
            int_now_temp = Math.round(get_now_temp).toInt()
            println("프래그먼트 1 현재온도 :  ${int_now_temp}")
        }


    }

    //뷰가 생성됐을 때 화면과 서로 연결
    //프래그먼트와 레이아웃을 연결시킨다
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_bottomnav1, container, false)
    }

    companion object {
        fun newInstance() : BottomnavFragment1{//프래그먼트 자기자신을 가져오는 함수
            return BottomnavFragment1()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        setClosetList()//해당 기온에 맞는 옷들을 담는 리스트를 구현한다
        //네이버 쇼핑 api를 가져오는 함수를 호출한다
        //코루틴으로 코딩해보자(동기방식)

        for(str in closetList){
            println("${str} 함수 이동")
            set_coroutine(str)
        }

        val adapter = Fragment1OutAdapter(setting_closetList)
        now_temp_rv.adapter = adapter
        //버튼을 누르면 여기서 화면이 전환되게끔
        now_temp_rv.layoutManager = LinearLayoutManager(context).also {
            it.orientation = LinearLayoutManager.VERTICAL
        }
        super.onActivityCreated(savedInstanceState)
    }

    private fun set_coroutine(closet : String){
        println("들어온 옷 : ${closet}")
        CoroutineScope(Dispatchers.Main).launch {
            var closetJSON = CoroutineScope(Dispatchers.Default).async {
                //서버단의 작업
                var closjon = setInRecyclerView(closet)
                println("JSON Parsing${closet} : ${closjon}")
            }.await()

            println("${closet} 작업 : ${closetJSON}")
            //ui단의 작업
            //작은 rv 작업
            setting_closetList.add(Fragment1OutData(closet))
        }
    }

    private fun setClosetList(){
        if(int_now_temp <= 4){
            closetList = listOf("패딩","두꺼운 코트","기모티셔츠","두꺼운니트","니트","맨투맨","기모바지")
        }else if(int_now_temp in 5..8){
            closetList = listOf("코트","가죽자켓","니트","맨투맨","청바지","기모바지")
        }else if(int_now_temp in 9..11){
            closetList = listOf("자켓","야상","트랜치코트","니트","맨투맨","청바지")
        }else if(int_now_temp in 12..14){
            closetList = listOf("자켓","야상","트랜치코트","가디건","니트","맨투맨","청바지","면바지")
        }else if(int_now_temp in 15..16){
            closetList = listOf("자켓","야상","가디건","니트","맨투맨","청바지","면바지")
        }else if(int_now_temp == 17){
            closetList = listOf("가디건","맨투맨","얇은니트","청바지","면바지")
        }else if(int_now_temp in 18..19){
            closetList = listOf("가디건","얇은 가디건","맨투맨","긴팔","긴 셔츠","얇은니트","청바지","면바지")
        }else if(int_now_temp in 20..22){
            closetList = listOf("얇은 가디건","긴팔","긴 셔츠","청바지","면바지")
        }else if(int_now_temp in 23..27){
            closetList = listOf("반팔","반 셔츠","린넨셔츠","면바지")
        }else if(int_now_temp >= 28){
            closetList = listOf("민소매","반팔","반 셔츠","린넨셔츠","반바지")
        }

    }

    //네이버 쇼핑 api를 가져온 후 안쪽 RecyclerView를 설정하는 메서드를 만들자
    private fun setInRecyclerView(keyword : String) : String{
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

        return responseBody.toString()
    }

    private operator fun get(
        apiUrl: String,
        requestHeaders: Map<String, String>
    ): String? {
        val con: HttpURLConnection = connect(apiUrl)
        return try {
            con.setRequestMethod("GET")
            for ((key, value) in requestHeaders) {
                con.setRequestProperty(key, value)
            }
            val responseCode: Int = con.getResponseCode()
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
                var line: String? = lineReader.readLine()
                while (lineReader.readLine().also({ line = it }) != null) {
                    responseBody.append(line)
                }
                return responseBody.toString()
            }
        } catch (e: IOException) {
            throw java.lang.RuntimeException("API 응답을 읽는데 실패했습니다.", e)
        }
    }

}