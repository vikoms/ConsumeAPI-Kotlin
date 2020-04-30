package com.example.consumelaravelapi.entity

import com.google.gson.annotations.SerializedName

data class User (
    var email: String? = null,
    var username: String? = null,

    @SerializedName("user_id")
    var id: Int? = 0,

    var token: String?= null
)