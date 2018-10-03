package com.eugenetereshkov.testtinkoff.presenter.depositionpointslist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner


@StateStrategyType(AddToEndSingleStrategy::class)
interface DepositionPointsListView : MvpView {
    fun showCountItems(data: List<DepositionPointAndPartner>)
    fun showError(error: String)
}
