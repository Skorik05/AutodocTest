package com.skorik05.autodoctest.model.api

import com.skorik05.autodoctest.model.items.ListOfRepositoriesItem
import com.skorik05.autodoctest.model.items.UserItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @GET("search/repositories")
    suspend fun getRepositoriesByName(
        @Query(QUERY_PARAM_NAME) name: String = "",
        @Query(QUERY_PARAM_PER_PAGE) perPage: Int = 10,
        @Query(QUERY_PARAM_PAGE) page: Int = 1
    ) : Response<ListOfRepositoriesItem>

    @GET
    suspend fun getUser(
        @Url userUrl: String = ""
    ) :Response<UserItem>

    companion object {
        private const val QUERY_PARAM_NAME = "q"
        private const val QUERY_PARAM_PER_PAGE = "per_page"
        private const val QUERY_PARAM_PAGE = "page"
    }
}