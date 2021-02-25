package com.example.worldskills.database

object ShablonEntry {
    const val TABLE_NAME = "shablons"
    const val COLUMN_ID = "id"
    const val COLUMN_NAME = "name"
    const val COLUMN_SUM = "sum"

    const val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_ID INT PRIMARY KEY," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_SUM INT NOT NULL)"

    const val DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}