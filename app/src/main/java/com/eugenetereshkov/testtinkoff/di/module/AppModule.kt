package com.eugenetereshkov.testtinkoff.di.module

import android.content.Context
import com.eugenetereshkov.testtinkoff.App
import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.eugenetereshkov.testtinkoff.model.data.db.DepositionPartnerEntity
import com.eugenetereshkov.testtinkoff.model.data.db.HelperFactory
import com.eugenetereshkov.testtinkoff.model.system.AppPreferences
import com.j256.ormlite.dao.Dao
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

    @Singleton
    @Provides
    fun providePointDao(helperFactory: HelperFactory): Dao<DepositionPoint, Int> =
            helperFactory.databaseHelper.getDao(DepositionPoint::class.java)

    @Singleton
    @Provides
    fun providePartnerDao(helperFactory: HelperFactory): Dao<DepositionPartnerEntity, Int> =
            helperFactory.databaseHelper.getDao(DepositionPartnerEntity::class.java)
}
