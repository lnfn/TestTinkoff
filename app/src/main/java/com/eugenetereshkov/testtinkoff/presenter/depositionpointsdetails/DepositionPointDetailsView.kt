package com.eugenetereshkov.testtinkoff.presenter.depositionpointsdetails

import com.arellomobile.mvp.MvpView
import com.eugenetereshkov.testtinkoff.entity.DepositionPointAndPartner


interface DepositionPointDetailsView : MvpView {
    fun showData(data: DepositionPointAndPartner)
}
