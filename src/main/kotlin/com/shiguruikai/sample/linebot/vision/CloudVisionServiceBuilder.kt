package com.shiguruikai.sample.linebot.vision

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class CloudVisionServiceBuilder(private val apiKey: String) {

    val BASE_URL = "https://vision.googleapis.com/"

    fun build(): CloudVisionService {

        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
                .addInterceptor { chain ->
                    chain.request().let {
                        val newUrl = it.url().newBuilder().addQueryParameter("key", apiKey).build()
                        val newRequest = it.newBuilder().url(newUrl).build()
                        chain.proceed(newRequest)
                    }
                }
                .build()

        val objectMapper = jacksonObjectMapper()
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()

        return retrofit.create(CloudVisionService::class.java)
    }
}
