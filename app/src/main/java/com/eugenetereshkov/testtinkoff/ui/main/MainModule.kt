package com.eugenetereshkov.testtinkoff.ui.main

import android.app.Activity
import com.eugenetereshkov.testtinkoff.di.scope.ActivityScoped
import com.eugenetereshkov.testtinkoff.di.scope.FragmentScoped
import com.eugenetereshkov.testtinkoff.presenter.depositionpointscontainer.DepositionPointsContainerModule
import com.eugenetereshkov.testtinkoff.ui.depositionpointscontainer.DepositionPointsContainerFragment
import com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails.DepositionPointsDetailsFragment
import com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails.DepositionPointsDetailsModule
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface MainModule {

    @Binds
    @ActivityScoped
    fun activity(mainActivity: MainActivity): Activity

    @FragmentScoped
    @ContributesAndroidInjector(modules = [DepositionPointsContainerModule::class])
    fun contributeDepositionPointsContainerFragment(): DepositionPointsContainerFragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = [DepositionPointsDetailsModule::class])
    fun contributeDepositionPointsDetailsFragment(): DepositionPointsDetailsFragment
}
