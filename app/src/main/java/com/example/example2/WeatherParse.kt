package com.example.example2

/* JSON 데이터를 파싱하기 위한 클래스 */
data class WeatherParse(var current : Current ?= null) {
    data class Current(var temp : Double)
}