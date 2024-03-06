package com.isaev.dummyjson

import kotlinx.serialization.Serializable

@Serializable
data class ProductsResponse(
    val products: List<Product>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@Serializable
data class Product(
    var id: Int,
    var title: String,
    var description: String,
    var price: Int,
    var discountPercentage: Double,
    var rating: Double,
    var stock: Int,
    var brand: String,
    var category: String,
    var thumbnail: String,
    var images: List<String>
)
