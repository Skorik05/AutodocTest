package com.skorik05.autodoctest.model.items

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class OwnerItem (
    @SerializedName("avatar_url")
    @Expose
    val avatarUrl: String? = null,

    @SerializedName("url")
    @Expose
    val url: String? = null
)