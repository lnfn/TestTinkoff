package com.eugenetereshkov.testtinkoff.presenter.depositionpointslist

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType


@StateStrategyType(AddToEndSingleStrategy::class)
interface DepositionPointsListView : MvpView {
    fun showCountItems(size: Int)
}
