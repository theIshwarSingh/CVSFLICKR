package com.interview.myapplication.data

import com.google.gson.annotations.SerializedName


data class Posts(
    @SerializedName("link")
    val link: String,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("author")
    val author: String,

    @SerializedName("date_taken")
    val dataTaken : String
)
