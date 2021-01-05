package com.lofrus.githubuserappv3.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserFav::class], version = 1, exportSchema = false)
abstract class UserFavDatabase : RoomDatabase() {
    abstract fun usersFav(): UserFavDao

    companion object {
        private var INSTANCE: UserFavDatabase? = null
        fun getAppDatabase(context: Context): UserFavDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    UserFavDatabase::class.java, "room-kotlin-database"
                ).build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}