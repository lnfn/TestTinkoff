package com.eugenetereshkov.testtinkoff.ui.main

import com.eugenetereshkov.testtinkoff.di.scope.FragmentScoped
import com.eugenetereshkov.testtinkoff.presenter.depositionpointscontainer.DepositionPointsContainerModule
import com.eugenetereshkov.testtinkoff.ui.depositionpointscontainer.DepositionPointsContainerFragment
import com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails.DepositionPointsDetailsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface MainModule {
    @FragmentScoped
    @ContributesAndroidInjector(modules = [DepositionPointsContainerModule::class])
    fun contributeDepositionPointsContainerFragment(): DepositionPointsContainerFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun contributeDepositionPointsDetailsFragment(): DepositionPointsDetailsFragment
}
