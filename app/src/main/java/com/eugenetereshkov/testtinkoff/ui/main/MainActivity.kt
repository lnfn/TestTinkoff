package com.eugenetereshkov.testtinkoff.ui.main

import android.os.Bundle
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.eugenetereshkov.testtinkoff.R
import com.eugenetereshkov.testtinkoff.presenter.main.MainPresenter
import com.eugenetereshkov.testtinkoff.ui.global.BaseActivity
import dagger.android.AndroidInjection
import javax.inject.Inject


class MainActivity : BaseActivity() {

    override val idResLayout: Int = R.layout.activity_main

    @Inject
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter = presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        presenter.test()
    }
}
