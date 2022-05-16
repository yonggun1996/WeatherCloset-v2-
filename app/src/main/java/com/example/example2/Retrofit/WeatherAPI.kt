package com.example.example2.Retrofit

import com.example.example2.BottomNavigationFragment3.WeatherParse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("onecall?")
    fun getWeather(
        @Query("lat") lat: Double = 0.0,
        @Query("lon") lon: Double = 0.0,
        @Query("exclude") exclude: String,
        @Query("appid") appid: String,
        @Query("units") units: String
    ): Call<WeatherParse>

    companion object{
        private const val BASEURL = "https://api.openweathermap.org/data/2.5/"

        fun create_Weather(): WeatherAPI{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WeatherAPI::class.java)
        }
    }
}