package com.example.majika.network

import com.example.majika.models.MenuApiModel
import com.example.majika.models.PaymentModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://7d96-180-253-254-149.ap.ngrok.io/v1/"

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

    @POST("payment/{code}")
    suspend fun postPayment(@Path("code") code: String): PaymentModel

}

object Api {
    val retrofitService : ApiService by lazy { retrofit.create(ApiService::class.java) }
}