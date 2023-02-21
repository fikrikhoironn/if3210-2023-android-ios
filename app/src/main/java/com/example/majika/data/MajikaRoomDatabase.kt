package com.example.majika.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.majika.data.dao.CartDao
import com.example.majika.data.entity.Cart

@Database(entities = [Cart::class], version = 1)
abstract class MajikaRoomDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao

    companion object {
        private var instance: MajikaRoomDatabase? = null

        fun getInstance(context: Context): MajikaRoomDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, MajikaRoomDatabase::class.java, "majika-room-database")
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return instance!!
        }
    }
}