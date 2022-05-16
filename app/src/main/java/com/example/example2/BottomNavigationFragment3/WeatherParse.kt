package com.example.example2.BottomNavigationFragment3

/* JSON 데이터를 파싱하기 위한 클래스 */
data class WeatherParse(var current : Current?= null, var hourly : Array<Hourly>) {
    data class Current(var temp : Double, var feels_like : Double, var weather : Array<Weather>){
        data class Weather(var id : Int, var main : String, var description : String, var icon : String){

        }
    }

    data class Hourly(var dt : Long ?= null, var temp : Double ?= null, var weather : Array<Weather>){
        data class Weather(var id : Int, var main : String, var description : String, var icon : String){

        }
    }
}