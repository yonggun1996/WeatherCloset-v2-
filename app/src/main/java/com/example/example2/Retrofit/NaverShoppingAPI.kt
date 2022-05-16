package com.example.example2.Retrofit

import android.util.Log
import com.example.example2.BottomNavigationFragment1.ShoppingParse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverShoppingAPI {
    @GET("search/shop.json")//기본 URL에 어떠한 정보를 얻어올 것인지와 HTTP 메소드를 설정
    fun get_ShoppingData(
        @Query("query") query : String?,//호출한 액티비티에서 입력받은 쿼리를 받아온다
        @Query("display") display : Int? = 10//호출한 액티비티에서 입력받은 결과 수를 받아온다
    ): Call<ShoppingParse>

    companion object{//싱글톤 패턴으로 다른 액티비티에서 호출해도 동일한 인스턴스를 가질 수 있게 한다
        private const val BASEURL = "https://openapi.naver.com/v1/"//기본이 될 URL

        //네이버 api를 파싱하기 위해선 id와 비밀 키가 있어야 한다
        private const val CLIENT_ID = ""
        private const val CLIENT_SERECT = ""
        private const val TAG = "NaverShoppingAPI"

        fun create(): NaverShoppingAPI{
            //로깅 인터셉터 추가
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor{
                Log.d(TAG, "Interceptor Chain : ${it}")
                val request = it.request()
                    .newBuilder()
                    .addHeader("X-Naver-Client-Id", CLIENT_ID)
                    .addHeader("X-Naver-Client-Secret", CLIENT_SERECT)
                    .build()
                return@Interceptor  it.proceed(request)
            }

            //레트로핏 클라이언트 선언
            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()

            //레트로핏을 이용해 URL에 접속해 데이터를 얻어온다
            return Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())//JSON 데이터를 직렬화 하기 위해 GsonConverterFactory선언
                .build()
                .create(NaverShoppingAPI::class.java)
        }
    }

}