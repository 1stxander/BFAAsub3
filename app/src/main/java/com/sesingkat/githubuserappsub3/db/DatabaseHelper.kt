package com.sesingkat.githubuserappsub3.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns
import com.sesingkat.githubuserappsub3.db.UserContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbuserapp"

        private const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${UserColumns.COLUMN_NAME_USERNAME} TEXT PRIMARY KEY NOT NULL," +
                " ${UserColumns.COLUMN_NAME_AVATAR} TEXT NOT NULL," +
                " ${UserColumns.COLUMN_NAME_NAME} TEXT NOT NULL," +
                " ${UserColumns.COLUMN_NAME_LOCATION} TEXT NOT NULL," +
                " ${UserColumns.COLUMN_NAME_COMPANY} TEXT NOT NULL," +
                " ${UserColumns.COLUMN_NAME_REPOSITORY} TEXT NOT NULL," +
                " ${UserColumns.COLUMN_NAME_FAVORITE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}