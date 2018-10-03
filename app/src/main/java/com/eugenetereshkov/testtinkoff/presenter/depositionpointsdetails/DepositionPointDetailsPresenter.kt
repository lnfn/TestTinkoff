package com.eugenetereshkov.testtinkoff.presenter.depositionpointsdetails

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenetereshkov.testtinkoff.di.qualifier.DepositionPointDetailsData
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner
import javax.inject.Inject


@InjectViewState
class DepositionPointDetailsPresenter @Inject constructor(
        @DepositionPointDetailsData private val data: DepositionPointAndPartner
) : MvpPresenter<DepositionPointDetailsView>() {

    override fun onFirstViewAttach() {
        viewState.showData(data)
    }
}
