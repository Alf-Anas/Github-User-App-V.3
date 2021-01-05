package com.lofrus.githubuserappv3.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.lofrus.githubuserappv3.room.DBContract.AUTHORITY
import com.lofrus.githubuserappv3.room.DBContract.UserFavColumns.Companion.CONTENT_URI
import com.lofrus.githubuserappv3.room.UserFav
import com.lofrus.githubuserappv3.room.UserFav.Companion.TABLE_NAME
import com.lofrus.githubuserappv3.room.UserFavDatabase

class UserFavProvider : ContentProvider() {

    companion object {
        private const val USER_ALL = 1
        private const val USER_ID = 2
        private lateinit var localDb: UserFavDatabase
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER_ALL)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    override fun onCreate(): Boolean {
        localDb = UserFavDatabase.getAppDatabase(context as Context)!!
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (sUriMatcher.match(uri)) {
            USER_ALL -> localDb.usersFav().getAllFavUsers()
            USER_ID -> uri.lastPathSegment?.let { localDb.usersFav().getFavUserWithId(it.toInt()) }
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return when (sUriMatcher.match(uri)) {
            USER_ID -> {
                val context = context ?: return null
                val added = localDb.usersFav().insert(UserFav().fromContentValues(values))
                context.contentResolver.notifyChange(CONTENT_URI, null)
                Uri.parse("$CONTENT_URI/$added")
            }
            USER_ALL -> throw IllegalArgumentException("Invalid URI, cannot insert with ID: $uri")
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> localDb.usersFav()
                .deleteUserWithId(uri.lastPathSegment!!.toInt())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

}