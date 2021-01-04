package com.sesingkat.githubuserappsub3.db

import android.net.Uri
import android.provider.BaseColumns

internal object UserContract {

    const val AUTHORITY = "com.sesingkat.githubuserappsub3"
    const val SCHEME = "content"

    internal class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_NAME_AVATAR = "avatar"
            const val COLUMN_NAME_USERNAME = "username"
            const val COLUMN_NAME_NAME = "name"
            const val COLUMN_NAME_LOCATION = "location"
            const val COLUMN_NAME_COMPANY = "company"
            const val COLUMN_NAME_REPOSITORY = "repository"
            const val COLUMN_NAME_FAVORITE = "isFav"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}