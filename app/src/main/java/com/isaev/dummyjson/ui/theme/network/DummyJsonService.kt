package com.isaev.dummyjson.ui.theme.network

import com.isaev.dummyjson.ProductsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DummyJsonService {

    @GET("products")
    suspend fun getProducts(
        @Query("skip") skip: Int, @Query("limit") limit: Int
    ): ProductsResponse
}