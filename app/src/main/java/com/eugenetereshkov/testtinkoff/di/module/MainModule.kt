package com.eugenetereshkov.testtinkoff.di.module

import com.eugenetereshkov.testtinkoff.di.scope.FragmentScoped
import com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails.DepositionPointsDetailsFragment
import com.eugenetereshkov.testtinkoff.ui.depositionpointslist.DepositionPointsListFragment
import com.eugenetereshkov.testtinkoff.ui.depositionpointsmap.DepositionPointsMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface MainModule {

    @FragmentScoped
    @ContributesAndroidInjector
    fun contributeDepositionPointsListFragment(): DepositionPointsListFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun contributeDepositionPointsMapFragment(): DepositionPointsMapFragment

    @FragmentScoped
    @ContributesAndroidInjector
    fun contributeDepositionPointsDetailsFragment(): DepositionPointsDetailsFragment
}
