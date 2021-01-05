package com.lofrus.githubuserfavoriteapp.model

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lofrus.githubuserfavoriteapp.model.UserFav.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class UserFav(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var login: String = "",
    var avatar_url: String = "",
    var html_url: String = ""
) {
    companion object {
        const val TABLE_NAME = "users_fav"
        const val COLUMN_ID = "id"
        const val COLUMN_LOGIN = "login"
        const val COLUMN_AVATAR_URL = "avatar_url"
        const val COLUMN_HTML_URL = "html_url"
    }

    fun fromContentValues(values: ContentValues?): UserFav {
        val userFav = UserFav()
        if (values != null && values.containsKey(COLUMN_ID)) {
            userFav.id = values.getAsLong(COLUMN_ID).toInt()
        }
        if (values != null && values.containsKey(COLUMN_LOGIN)) {
            userFav.login = values.getAsString(COLUMN_LOGIN)
        }
        if (values != null && values.containsKey(COLUMN_AVATAR_URL)) {
            userFav.avatar_url = values.getAsString(COLUMN_AVATAR_URL)
        }
        if (values != null && values.containsKey(COLUMN_HTML_URL)) {
            userFav.html_url = values.getAsString(COLUMN_HTML_URL)
        }
        return userFav
    }
}