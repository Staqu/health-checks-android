package com.staqu.healthmonitoring.network

import com.staqu.healthmonitoring.network.model.CheckResponse
import io.reactivex.Single
import retrofit2.http.GET


interface ApiService {

    //Fetch all existing checks
    @GET("checks/")
    fun listAllChecks(): Single<CheckResponse>

}