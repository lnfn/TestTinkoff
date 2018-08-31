package com.eugenetereshkov.testtinkoff.model.data.db

import com.eugenetereshkov.testtinkoff.entity.DailyLimit
import com.eugenetereshkov.testtinkoff.entity.Limit


class LimitsWrapper {
    var limits: List<Limit> = emptyList()
    var dailyLimits: List<DailyLimit> = emptyList()
}
