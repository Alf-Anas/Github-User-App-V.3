package com.lofrus.githubuserappv3.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_fav")
data class UserFav(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var login: String = "",
    var avatar_url: String = "",
    var html_url: String = ""
)