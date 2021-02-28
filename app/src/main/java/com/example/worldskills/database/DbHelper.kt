package com.example.worldskills.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(ShablonEntry.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL(ShablonEntry.DELETE_TABLE)
        db.execSQL(ShablonEntry.CREATE_TABLE)
    }

    companion object {
        const val DATABASE_NAME = "db.db"
        const val DATABASE_VERSION = 4
    }
}