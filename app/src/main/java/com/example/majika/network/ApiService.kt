package com.example.majika.network

import com.example.majika.models.MenuApiModel
import com.example.majika.models.MenuModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "http://localhost/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("menu/food")
    suspend fun getMenuFood(): MenuApiModel

    @GET("menu/drink")
    suspend fun getMenuDrink(): MenuApiModel

    @POST("payment/:code")
    suspend fun postPayment(code: String): MenuApiModel
}

object Api {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}