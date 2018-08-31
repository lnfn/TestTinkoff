package com.eugenetereshkov.testtinkoff


import com.eugenetereshkov.testtinkoff.di.DaggerAppComponent
import com.eugenetereshkov.testtinkoff.entity.DepositionPoint
import com.eugenetereshkov.testtinkoff.model.data.db.HelperFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject


class App : DaggerApplication() {

    @Inject
    lateinit var helperFactory: HelperFactory

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        helperFactory.databaseHelper.getDao(DepositionPoint::class.java)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this).also {
            it.inject(this)
        }
    }

    override fun onTerminate() {
        helperFactory.releaseHelper()
        super.onTerminate()
    }
}
