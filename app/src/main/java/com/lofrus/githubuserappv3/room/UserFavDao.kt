package com.lofrus.githubuserappv3.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserFavDao {
    @Insert
    fun insert(user:UserFav)

    @Update
    fun update(user:UserFav)

    @Update
    fun delete(user:UserFav)

    @Query("DELETE FROM USERS_FAV WHERE id==:id")
    fun deleteUserWithId(id:Int)

    @Query("SELECT * FROM USERS_FAV")
    fun getAllUsers():List<UserFav>

    @Query("SELECT * FROM USERS_FAV WHERE id==:id")
    fun getUserWithId(id:Int):UserFav?
}