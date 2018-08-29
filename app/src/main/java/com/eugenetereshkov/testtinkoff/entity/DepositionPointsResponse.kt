package com.eugenetereshkov.testtinkoff.entity


data class DepositionPointsResponse(
        override val payload: List<DepositionPoint>
) : TinkoffApiResponse<List<DepositionPoint>>()
