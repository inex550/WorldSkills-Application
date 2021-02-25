package com.example.worldskills.database

import com.example.worldskills.models.Shablon

object Crud {

    fun selectAllShablons(dbHelper: DbHelper): List<Shablon> {
        val shablons = mutableListOf<Shablon>()
        val db = dbHelper.readableDatabase

        val query = "SELECT * FROM ${ShablonEntry.TABLE_NAME}"

        with(db.rawQuery(query, null)) {
            while (moveToNext()) {
                val shablon = Shablon(
                        id = getInt(getColumnIndex(ShablonEntry.COLUMN_ID)),
                        name = getString(getColumnIndex(ShablonEntry.COLUMN_NAME)),
                        sum = getInt(getColumnIndex(ShablonEntry.COLUMN_SUM))
                )
                shablons.add(shablon)
            }
        }

        return shablons
    }
}