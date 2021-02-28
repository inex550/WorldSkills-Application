package com.example.worldskills.database

import android.content.ContentValues
import android.provider.BaseColumns
import com.example.worldskills.models.Shablon

object Crud {

    fun selectAllShablons(dbHelper: DbHelper): List<Shablon> {
        val shablons = mutableListOf<Shablon>()
        val db = dbHelper.readableDatabase

        val query = "SELECT * FROM ${ShablonEntry.TABLE_NAME}"

        with(db.rawQuery(query, null)) {
            while (moveToNext()) {
                val shablon = Shablon(
                        id = getInt(getColumnIndex(BaseColumns._ID)),
                        name = getString(getColumnIndex(ShablonEntry.COLUMN_NAME)),
                        sum = getInt(getColumnIndex(ShablonEntry.COLUMN_SUM))
                )
                shablons.add(shablon)
            }
        }

        return shablons
    }

    fun deleteShablon(dbHelper: DbHelper, shablon: Shablon) {
        val db = dbHelper.writableDatabase

        db.delete(ShablonEntry.TABLE_NAME, "${BaseColumns._ID} = ?", arrayOf(shablon.id.toString()))

        db.close()
    }

    fun updateShablon(dbHelper: DbHelper, shablon: Shablon) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(ShablonEntry.COLUMN_NAME, shablon.name)
            put(ShablonEntry.COLUMN_SUM, shablon.sum)
        }

        db.update(ShablonEntry.TABLE_NAME, values, "${BaseColumns._ID} = ?", arrayOf(shablon.id.toString()))
    }
}