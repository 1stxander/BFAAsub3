package com.sesingkat.githubuserappsub3.db

import android.database.Cursor
import com.sesingkat.githubuserappsub3.FavData

object MappingHelper {

    fun mapCursorToArrayList(userCursor: Cursor?): ArrayList<FavData> {
        val favoriteList = ArrayList<FavData>()

        userCursor?.apply {
            while (moveToNext()) {
                val avatar = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_AVATAR))
                val username = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_USERNAME))
                val name = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_NAME))
                val location= getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_LOCATION))
                val company = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_COMPANY))
                val repository = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_REPOSITORY))
                val favorite = getString(getColumnIndexOrThrow(UserContract.UserColumns.COLUMN_NAME_FAVORITE))
                favoriteList.add(
                    FavData(
                        avatar, username, name, location, company, repository, favorite
                    )
                )
            }
        }
        return favoriteList
    }
}