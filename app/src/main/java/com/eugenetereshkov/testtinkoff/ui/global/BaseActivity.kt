package com.eugenetereshkov.testtinkoff.ui.global

import android.os.Bundle
import android.support.annotation.LayoutRes
import com.arellomobile.mvp.MvpAppCompatActivity


abstract class BaseActivity : MvpAppCompatActivity() {
    @get:LayoutRes
    protected abstract val idResLayout: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(idResLayout)
    }
}
