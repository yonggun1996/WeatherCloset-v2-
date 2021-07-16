package com.example.example2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import com.example.example2.databinding.ActivityMoresearchBinding
import kotlinx.android.synthetic.main.activity_moresearch.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.net.URLEncoder
import java.util.HashMap

class MoresearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoresearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moresearch)

        CoroutineScope(Dispatchers.Main).launch {
            //뷰 바인딩 작업을 하기 위해 Root View를 참조하는 과정정
            binding = ActivityMoresearchBinding.inflate(layoutInflater)
            val view = binding.root
            setContentView(view)
            binding.moresearchProgressbar.visibility = View.VISIBLE

            var closet = intent.getStringExtra("closet")
            var json_result = ""

            var getjson = CoroutineScope(Dispatchers.Default).launch {
                json_result = get_json(closet)
            }.join()

            binding.moresearchProgressbar.visibility = View.INVISIBLE
            println(json_result)
        }

    }
    
    private fun get_json(closet : String?) : String{
        val clientId = "" //애플리케이션 클라이언트 아이디값"

        val clientSecret = "" //애플리케이션 클라이언트 시크릿값"


        var text: String? = null
        text = try {
            URLEncoder.encode(closet, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            throw RuntimeException("검색어 인코딩 실패", e)
        }


        val apiURL =
            "https://openapi.naver.com/v1/search/shop?query=${text}&display=30" // json 결과

        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과


        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
        val requestHeaders: MutableMap<String, String> = HashMap()
        requestHeaders["X-Naver-Client-Id"] = clientId
        requestHeaders["X-Naver-Client-Secret"] = clientSecret
        val responseBody: String? = get(apiURL, requestHeaders)

        var result = Html.fromHtml(responseBody, Html.FROM_HTML_MODE_LEGACY).toString()

        return result
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
                print("불러오는데서 에러 발생 ${responseCode}")
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