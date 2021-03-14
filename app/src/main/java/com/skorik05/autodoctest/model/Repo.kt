package com.skorik05.autodoctest.model

import com.skorik05.autodoctest.model.api.ApiHelper

class Repo(private val apiHelper: ApiHelper) {

    suspend fun getRepositoriesByName(name: String, perPage: Int, page: Int) = apiHelper.getRepositoriesByName(name, perPage, page)
    suspend fun getUser(userUrl: String) = apiHelper.getUser(userUrl)
}