package com.eugenetereshkov.testtinkoff.entity


class DepositionPartnersResponse(
        override val payload: List<DepositionPartner>
) : TinkoffApiResponse<List<DepositionPartner>>()
