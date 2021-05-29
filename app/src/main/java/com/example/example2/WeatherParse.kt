package com.example.example2

import org.json.JSONArray

/* JSON 데이터를 파싱하기 위한 클래스 */
data class WeatherParse(var current : Current ?= null) {
    data class Current(var temp : Double, var feels_like : Double, var weather : Array<Weather>){
        data class Weather(var id : Int, var main : String, var description : String, var icon : String){

        }
    }
}