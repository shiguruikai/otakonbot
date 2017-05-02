package com.shiguruikai.sample.linebot.vision

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CloudVisionService {

    @POST("v1/images:annotate")
    fun post(@Body visionRequest: VisionRequest): Call<VisionResponse>
}


