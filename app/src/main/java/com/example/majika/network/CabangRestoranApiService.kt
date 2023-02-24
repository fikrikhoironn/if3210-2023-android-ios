package com.example.majika.network

import com.example.majika.models.CabangRestoranApiModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://7d96-180-253-254-149.ap.ngrok.io/v1/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CabangRestoranApiService {
    @GET("branch")
    suspend fun getCabangRestoran(): CabangRestoranApiModel
}

object CabangRestoranApi {
    val retrofitService: CabangRestoranApiService by lazy { retrofit.create(CabangRestoranApiService::class.java) }
}