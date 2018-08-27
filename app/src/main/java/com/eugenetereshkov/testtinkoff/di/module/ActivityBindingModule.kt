package com.eugenetereshkov.testtinkoff.di.module

import com.eugenetereshkov.testtinkoff.di.scope.ActivityScoped
import com.eugenetereshkov.testtinkoff.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ActivityBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = [MainModule::class])
    fun mainActivity(): MainActivity
}
