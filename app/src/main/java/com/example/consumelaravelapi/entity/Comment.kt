package com.example.consumelaravelapi.entity

import com.google.gson.annotations.SerializedName

data class Comment (
    var body:String? = null,
    var id: Int? =0,
    @SerializedName("user_id")
    var userId:Int? = 0,

    @SerializedName("tutorial_id")
    var tutorialId:Int? = 0
)