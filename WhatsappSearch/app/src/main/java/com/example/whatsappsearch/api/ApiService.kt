package com.example.whatsappsearch.api

import com.example.whatsappsearch.model.StateResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("LogisticsApi/GetStateList")
    suspend fun getStateList(): Response<StateResponse>
}
