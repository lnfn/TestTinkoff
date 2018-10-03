package com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails

import com.eugenetereshkov.testtinkoff.di.qualifier.DepositionPointDetailsData
import com.eugenetereshkov.testtinkoff.di.scope.FragmentScoped
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import com.eugenetereshkov.testtinkoff.ui.depositionpointsdetails.DepositionPointsDetailsFragment.Companion.DATA
import dagger.Module
import dagger.Provides


@Module
class DepositionPointsDetailsModule {
    @FragmentScoped
    @DepositionPointDetailsData
    @Provides
    fun provideDepositionPointsDetailsData(fragment: DepositionPointsDetailsFragment) =
            fragment.arguments?.getParcelable(DATA) as DepositionPointAndPartner
}
