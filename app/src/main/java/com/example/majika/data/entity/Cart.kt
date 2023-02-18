package com.example.majika.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Cart(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "currency")
    var currency: String?,

    @ColumnInfo(name = "price")
    var price: Int?,

    @ColumnInfo(name = "sold")
    var sold: Int?,

    @ColumnInfo(name = "type")
    var type: String?,

    @ColumnInfo(name = "totalInCart")
    var totalInCart: Int?,

    @ColumnInfo(name = "priceInCart")
    var priceInCart: Int?
)