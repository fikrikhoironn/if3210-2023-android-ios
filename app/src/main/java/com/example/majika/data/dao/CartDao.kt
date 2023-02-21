package com.example.majika.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.majika.data.entity.Cart

@Dao
interface CartDao {
    @Query("SELECT * FROM Cart")
    suspend fun getAll(): List<Cart>

    @Query("SELECT * FROM Cart WHERE name = :name")
    suspend fun loadByName(name: String): Cart

    @Query("SELECT SUM(priceInCart) FROM Cart")
    suspend fun calculatePrice(): Int?

    @Query("UPDATE Cart SET totalInCart = :totalInCart, priceInCart = :priceInCart WHERE name = :name ")
    suspend fun update(name: String, totalInCart: Int, priceInCart: Int)

    @Insert
    suspend fun insertAll(vararg cart: Cart)

    @Delete
    suspend fun delete(cart: Cart)
}