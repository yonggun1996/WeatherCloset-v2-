package com.example.example2

data class ShoppingParse(var display : Int, var items : Array<Items>) {
    data class Items(var title : String, var lprice : Int, var brand : String, var image : String, var link : String){

    }
}