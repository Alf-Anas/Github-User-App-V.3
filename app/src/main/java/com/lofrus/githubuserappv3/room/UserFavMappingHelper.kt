package com.lofrus.githubuserappv3.room

import android.database.Cursor
import com.lofrus.githubuserappv3.room.UserFav.Companion.COLUMN_AVATAR_URL
import com.lofrus.githubuserappv3.room.UserFav.Companion.COLUMN_HTML_URL
import com.lofrus.githubuserappv3.room.UserFav.Companion.COLUMN_ID
import com.lofrus.githubuserappv3.room.UserFav.Companion.COLUMN_LOGIN
import java.util.ArrayList

object UserFavMappingHelper {

    fun mapCursorToArrayList(cursor: Cursor?): ArrayList<UserFav> {
        val listUserFav = ArrayList<UserFav>()
        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
                val login = getString(getColumnIndexOrThrow(COLUMN_LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_AVATAR_URL))
                val htmlUrl = getString(getColumnIndexOrThrow(COLUMN_HTML_URL))
                listUserFav.add(UserFav(id, login, avatarUrl, htmlUrl))
            }
        }
        return listUserFav
    }

    fun mapCursorToObject(cursor: Cursor?): UserFav {
        var userFav = UserFav()
        cursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(COLUMN_ID))
            val login = getString(getColumnIndexOrThrow(COLUMN_LOGIN))
            val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_AVATAR_URL))
            val htmlUrl = getString(getColumnIndexOrThrow(COLUMN_HTML_URL))
            userFav = UserFav(id, login, avatarUrl, htmlUrl)
        }
        return userFav
    }
}