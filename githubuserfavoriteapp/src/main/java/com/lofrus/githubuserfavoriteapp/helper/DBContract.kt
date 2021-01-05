package com.lofrus.githubuserfavoriteapp.helper

import android.net.Uri
import android.provider.BaseColumns
import com.lofrus.githubuserfavoriteapp.model.UserFav.Companion.TABLE_NAME

object DBContract {
    const val AUTHORITY = "com.lofrus.githubuserappv3"
    const val SCHEME = "content"

    class UserFavColumns : BaseColumns {
        companion object {
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}