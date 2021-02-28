package com.example.worldskills.database

import android.provider.BaseColumns

object ShablonEntry: BaseColumns {
    const val TABLE_NAME = "shablons"
    const val COLUMN_NAME = "name"
    const val COLUMN_SUM = "sum"

    const val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_SUM INTEGER NOT NULL)"

    const val DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}