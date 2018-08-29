package com.eugenetereshkov.testtinkoff.di.module

import com.eugenetereshkov.testtinkoff.model.repository.DepositionPointsRepository
import com.eugenetereshkov.testtinkoff.model.repository.IDepositionPointsRepository
import com.eugenetereshkov.testtinkoff.model.system.AppSchedulers
import com.eugenetereshkov.testtinkoff.model.system.SchedulersProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton


@Module
interface RepositoryModule {

    @Singleton
    @Binds
    fun provideSchedulersProvider(appSchedulers: AppSchedulers): SchedulersProvider

    @Singleton
    @Binds
    fun provideDepositionPintsRepository(
            depositionPointsResponse: DepositionPointsRepository
    ): IDepositionPointsRepository
}
