package com.skorik05.autodoctest.model.dagger.modules

import com.skorik05.autodoctest.model.dagger.annotations.MainScope
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides

@Module
class PicassoModule {

    @MainScope
    @Provides
    fun picasso() :Picasso {
        return Picasso.get()
    }
}