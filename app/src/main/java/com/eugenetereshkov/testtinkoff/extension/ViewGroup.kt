package com.eugenetereshkov.testtinkoff.extension

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


fun ViewGroup.inflate(@LayoutRes idResLayout: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(this.context).inflate(idResLayout, this, attachToRoot)
