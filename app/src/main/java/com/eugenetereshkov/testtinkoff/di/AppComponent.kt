package com.eugenetereshkov.testtinkoff.di

import com.eugenetereshkov.testtinkoff.App
import com.eugenetereshkov.testtinkoff.di.module.ActivityBindingModule
import com.eugenetereshkov.testtinkoff.di.module.AppModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            ActivityBindingModule::class
        ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
