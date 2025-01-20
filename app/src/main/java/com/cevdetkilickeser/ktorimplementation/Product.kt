package com.cevdetkilickeser.ktorimplementation


import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int? = 0,
    val title: String? = "",
    val price: Double? = 0.0,
    val description: String? = "",
    val category: String? = "",
    val image: String? = "",
    val rating: Rating? = Rating()
)

@Serializable
data class Rating(
    val rate: Double? = 0.0,
    val count: Int? = 0
)