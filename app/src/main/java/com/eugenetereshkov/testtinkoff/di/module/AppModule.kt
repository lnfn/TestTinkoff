package com.eugenetereshkov.testtinkoff.di.module

import android.content.Context
import com.eugenetereshkov.testtinkoff.App
import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerDao
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerEntity
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPointDao
import com.eugenetereshkov.testtinkoff.model.data.db.HelperFactory
import com.eugenetereshkov.testtinkoff.model.system.AppPreferences
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

    @Singleton
    @Provides
    fun provideSharedPreferences(context: Context) =
            AppPreferences(context.getSharedPreferences(AppPreferences.APP_PREFERENCES, Context.MODE_PRIVATE))

    @Singleton
    @Provides
    fun provideHelperFactory(context: Context) = HelperFactory(context)

    @Provides
    fun provideDepositionPintDao(helperFactory: HelperFactory) =
            DepositionPointDao(helperFactory.databaseHelper.getDao(DepositionPoint::class.java))

    @Provides
    fun provideDepositionPartnerDao(helperFactory: HelperFactory) =
            DepositionPartnerDao(helperFactory.databaseHelper.getDao(DepositionPartnerEntity::class.java))
}
