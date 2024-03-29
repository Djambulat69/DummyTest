package com.isaev.dummyjson.network

import com.isaev.dummyjson.Product
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object DummyJsonServiceHelper {

    private const val BASE_URL = "https://dummyjson.com/"
    private val json: Json = Json { ignoreUnknownKeys = true }

    private val service = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
        json.asConverterFactory("application/json".toMediaType())
    ).build().create(DummyJsonService::class.java)

    suspend fun getProducts(skip: Int = 0): List<Product> {
        val response = service.getProducts(skip, limit = 20)

        return response.products
    }
}