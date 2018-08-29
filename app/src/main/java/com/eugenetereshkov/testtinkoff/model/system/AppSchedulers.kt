package com.eugenetereshkov.testtinkoff.model.system

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppSchedulers @Inject constructor() : SchedulersProvider {
    override fun ui() = AndroidSchedulers.mainThread()
    override fun computation() = Schedulers.computation()
    override fun trampoline() = Schedulers.trampoline()
    override fun newThread() = Schedulers.newThread()
    override fun io() = Schedulers.io()
}
