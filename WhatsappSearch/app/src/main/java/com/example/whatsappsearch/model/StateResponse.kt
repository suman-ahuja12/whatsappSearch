package com.example.whatsappsearch.model


data class StateResponse(
    val Status: Boolean,
    val Message: String,
    val Data: List<StateDataItem>
)

data class StateDataItem(
    val StateName: String,
    val Country: String,
)

