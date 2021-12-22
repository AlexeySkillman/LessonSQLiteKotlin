package com.example.lessonsqlitekotlin.db

import android.provider.BaseColumns

// https://developer.android.com/training/data-storage/sqlite#kotlin

object MyDbNameClass: BaseColumns {
    // Наша Таблица - Название
    const val TABLE_NAME = "my_table"
    // Наша Таблица - Колонки
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_NAME_IMAGE_URI = "uri"

    // Название файла нашей Таблица и Версия
    const val DATABASE_VERSION = 2
    const val DATABASE_NAME = "FeedReader.db"

    const val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_CONTENT TEXT, $COLUMN_NAME_IMAGE_URI TEXT)"

    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

}