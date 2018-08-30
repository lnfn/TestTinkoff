package com.eugenetereshkov.testtinkoff.presenter.depositionpointslist

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.eugenetereshkov.testtinkoff.extension.bindTo
import com.eugenetereshkov.testtinkoff.model.repository.IDepositionPointsRepository
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject


@InjectViewState
class DepositionPointsListPresenter @Inject constructor(
        private val depositionPointsRepository: IDepositionPointsRepository
) : MvpPresenter<DepositionPointsListView>() {

    private val disposable = CompositeDisposable()

    override fun onFirstViewAttach() {
        depositionPointsRepository.source
                .subscribe(
                        { viewState.showCountItems(it.size) },
                        { Timber.e(it) }
                )
                .bindTo(disposable)
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }
}
