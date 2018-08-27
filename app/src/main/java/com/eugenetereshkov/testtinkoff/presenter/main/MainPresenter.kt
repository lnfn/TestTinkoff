package com.eugenetereshkov.testtinkoff.presenter.main

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenetereshkov.testtinkoff.model.system.ResourceManager
import javax.inject.Inject


@InjectViewState
class MainPresenter @Inject constructor(
        private val resourceManager: ResourceManager
) : MvpPresenter<MainView>() {

    fun test() {}
}
