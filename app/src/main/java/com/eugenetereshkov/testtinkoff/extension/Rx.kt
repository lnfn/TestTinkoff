package com.eugenetereshkov.testtinkoff.extension

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable


fun Disposable.bindTo(disposable: CompositeDisposable) {
    disposable.add(this)
}
