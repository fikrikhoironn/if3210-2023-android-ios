package com.example.majika.models

data class MenuModel(
    val name: String?,
    val description: String?,
    val currency: String?,
    val price: Int?,
    val sold: Int?,
    val type: String?,
    var totalInCart: Int?,
    var priceInCart: Int?
)
