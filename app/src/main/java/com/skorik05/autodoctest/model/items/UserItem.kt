package com.skorik05.autodoctest.model.items

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class UserItem (
    @SerializedName("login")
    @Expose
    val login: String? = null,

    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("avatar_url")
    @Expose
    val avatarUrl: String? = null,

    @SerializedName("name")
    @Expose
    val name: String? = null,

    @SerializedName("blog")
    @Expose
    val blog: String? = null,

    @SerializedName("email")
    @Expose
    val email: String? = null,

    @SerializedName("bio")
    @Expose
    val bio: String? = null,

    @SerializedName("twitter_username")
    @Expose
    val twitterUsername: String? = null,

    @SerializedName("followers")
    @Expose
    val followers: Int? = null,

    @SerializedName("following")
    @Expose
    val following: Int? = null
)