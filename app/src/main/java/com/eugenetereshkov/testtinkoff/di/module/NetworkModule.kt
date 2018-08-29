package com.eugenetereshkov.testtinkoff.di.module

import com.eugenetereshkov.testtinkoff.model.data.api.TinkoffApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideTinkoffApi(
            client: OkHttpClient,
            gson: Gson
    ): TinkoffApi = Retrofit.Builder().apply {
        client(client)
        baseUrl(TinkoffApi.BASE_URL)
        addConverterFactory(GsonConverterFactory.create(gson))
        addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    }.build().create(TinkoffApi::class.java)

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.HEADERS
                        level = HttpLoggingInterceptor.Level.BODY
                    }
            ).build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()
}
