package com.eugenetereshkov.testtinkoff.presenter.depositionpointscontainer

import com.eugenetereshkov.testtinkoff.di.scope.ChildFragmentScoped
import com.eugenetereshkov.testtinkoff.ui.depositionpointslist.DepositionPointsListFragment
import com.eugenetereshkov.testtinkoff.ui.depositionpointsmap.DepositionPointsMapFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface DepositionPointsContainerModule {

    @ChildFragmentScoped
    @ContributesAndroidInjector
    fun contributeDepositionPointsListFragment(): DepositionPointsListFragment

    @ChildFragmentScoped
    @ContributesAndroidInjector
    fun contributeDepositionPointsMapFragment(): DepositionPointsMapFragment
}
