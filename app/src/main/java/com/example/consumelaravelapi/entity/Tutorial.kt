package com.example.consumelaravelapi.entity



data class Tutorial (
    var id: Int? = 0,
    var title:String?= null,
    var body:String? = null,
    var user_id:Int? =0 ,
    var comments: ArrayList<Comment>? = null
)