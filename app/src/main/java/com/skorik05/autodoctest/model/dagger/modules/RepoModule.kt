package com.skorik05.autodoctest.model.dagger.modules

import com.skorik05.autodoctest.model.Repo
import com.skorik05.autodoctest.model.api.ApiHelper
import com.skorik05.autodoctest.model.api.ApiService
import com.skorik05.autodoctest.model.api.RetrofitBuilder
import com.skorik05.autodoctest.model.dagger.annotations.MainScope
import dagger.Module
import dagger.Provides

@Module
class RepoModule {

    @MainScope
    @Provides
    fun repo() : Repo {
        return Repo(apiHelper())
    }

    @Provides
    fun apiHelper() : ApiHelper {
        return ApiHelper(apiService())
    }

    @Provides
    fun apiService() : ApiService {
        return RetrofitBuilder.apiService
    }
}