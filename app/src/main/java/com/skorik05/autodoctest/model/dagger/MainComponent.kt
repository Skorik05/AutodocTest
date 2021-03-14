package com.skorik05.autodoctest.model.dagger

import com.skorik05.autodoctest.model.Repo
import com.skorik05.autodoctest.model.dagger.annotations.MainScope
import com.skorik05.autodoctest.model.dagger.modules.PicassoModule
import com.skorik05.autodoctest.model.dagger.modules.RepoModule
import com.squareup.picasso.Picasso
import dagger.Component

@MainScope
@Component(modules = [RepoModule::class, PicassoModule::class])
interface MainComponent {
    fun getRepo() : Repo
    fun getPicasso() : Picasso
}