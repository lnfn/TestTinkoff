package com.eugenetereshkov.testtinkoff.di.module

import android.content.Context
import com.eugenetereshkov.testtinkoff.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [
    NetworkModule::class,
    RepositoryModule::class
])
class AppModule {
    @Singleton
    @Provides
    fun provideContext(application: App): Context = application.applicationContext
}
