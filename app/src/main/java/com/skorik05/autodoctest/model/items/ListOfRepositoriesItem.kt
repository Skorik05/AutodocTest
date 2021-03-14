package com.skorik05.autodoctest.model.items

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ListOfRepositoriesItem (
    @SerializedName("items")
    @Expose
    val items: ArrayList<RepositoryItem>? = null
)