package com.skorik05.autodoctest.model.api

class ApiHelper(private val apiService: ApiService) {

    suspend fun getRepositoriesByName(name: String, perPage: Int, page: Int) = apiService.getRepositoriesByName(name, perPage, page)
    suspend fun getUser(userUrl: String) = apiService.getUser(userUrl)

}