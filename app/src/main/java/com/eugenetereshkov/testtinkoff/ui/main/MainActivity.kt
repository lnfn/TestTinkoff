package com.eugenetereshkov.testtinkoff.ui.main

import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.presenter.main.MainPresenter
import com.eugenetereshkov.testtinkoff.ui.depositionpointscontainer.DepositionPointsContainerFragment
import com.eugenetereshkov.testtinkoff.ui.global.BaseActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject


class MainActivity : BaseActivity(), HasSupportFragmentInjector {
    override val idResLayout: Int = R.layout.activity_main

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(
                            R.id.container,
                            DepositionPointsContainerFragment.newInstance(),
                            DepositionPointsContainerFragment.TAG
                    )
                    .commitNow()
        }
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}
