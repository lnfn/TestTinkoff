package com.eugenetereshkov.testtinkoff.presenter.depositionpointscontainer

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenetereshkov.testtinkoff.di.scope.FragmentScoped
import com.eugenetereshkov.testtinkoff.model.system.ResourceManager
import javax.inject.Inject


@FragmentScoped
@InjectViewState
class DepositionPointsContainerPresenter @Inject constructor(
        private val resourceManager: ResourceManager
) : MvpPresenter<DepositionPointsContainerView>() {

    var value = 0
        get() = ++field
}
