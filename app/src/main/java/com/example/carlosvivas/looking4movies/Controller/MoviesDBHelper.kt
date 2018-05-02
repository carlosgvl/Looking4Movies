package com.example.carlosvivas.looking4movies.Controller

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.carlosvivas.looking4movies.Model.MovieSq

/**
 * Created by carlosvivas on 5/1/18.
 */
class MoviesDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    @Throws(SQLiteConstraintException::class)
    fun insertUser(movie: MovieSq): Boolean {
        val db = writableDatabase

        val values = ContentValues()
        values.put(DBContract.UserEntry.COLUMN_VOTE_COUNT, movie.vote_count)
        values.put(DBContract.UserEntry.COLUMN_ID, movie.id)
        values.put(DBContract.UserEntry.COLUMN_VIDEO, movie.video)
        values.put(DBContract.UserEntry.COLUMN_VOTE_AVERAGE, movie.vote_average)
        values.put(DBContract.UserEntry.COLUMN_TITLE, movie.title)
        values.put(DBContract.UserEntry.COLUMN_POPULARITY, movie.popularity)
        values.put(DBContract.UserEntry.COLUMN_POSTER_PATH, movie.poster_path)
        values.put(DBContract.UserEntry.COLUMN_ORIGINAL_LANGUAGE, movie.original_language)
        values.put(DBContract.UserEntry.COLUMN_ORIGINAL_TITLE, movie.original_title)
        values.put(DBContract.UserEntry.COLUMN_GENRE_IDS, movie.genre_ids)
        values.put(DBContract.UserEntry.COLUMN_BACKDROP_PATH, movie.backdrop_path)
        values.put(DBContract.UserEntry.COLUMN_ADULT, movie.adult)
        values.put(DBContract.UserEntry.COLUMN_OVERVIEW, movie.overview)
        values.put(DBContract.UserEntry.COLUMN_RELEASE_DATE, movie.release_date)



        db.insert(DBContract.UserEntry.TABLE_NAME, null, values)
        val newRowId = db.insert(DBContract.UserEntry.TABLE_NAME, null, values)
        db.close()




        return true
    }

    @Throws(SQLiteConstraintException::class)
    fun deleteUser(userid: String): Boolean {

        val db = writableDatabase

        val selection = DBContract.UserEntry.COLUMN_ID + " LIKE ?"

        val selectionArgs = arrayOf(userid)

        db.delete(DBContract.UserEntry.TABLE_NAME, selection, selectionArgs)

        return true
    }

    fun readMovie(userid: String): ArrayList<MovieSq> {
        val users = ArrayList<MovieSq>()
        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + DBContract.UserEntry.TABLE_NAME + " WHERE " + DBContract.UserEntry.COLUMN_ID + "='" + userid + "'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                users.add(MovieSq(  cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_VOTE_COUNT)), cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ID)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_VIDEO)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_VOTE_AVERAGE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_TITLE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_POPULARITY)),cursor.getBlob(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_POSTER_PATH)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ORIGINAL_LANGUAGE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ORIGINAL_TITLE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_GENRE_IDS)),cursor.getBlob(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_BACKDROP_PATH)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ADULT)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_OVERVIEW)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_RELEASE_DATE))))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun readAllMovies(): ArrayList<MovieSq> {
        val users = ArrayList<MovieSq>()
        val db = readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM "+ DBContract.UserEntry.TABLE_NAME+ "'", null)
        } catch (e: SQLiteException) {
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                users.add(MovieSq(  cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_VOTE_COUNT)), cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ID)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_VIDEO)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_VOTE_AVERAGE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_TITLE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_POPULARITY)),cursor.getBlob(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_POSTER_PATH)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ORIGINAL_LANGUAGE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ORIGINAL_TITLE)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_GENRE_IDS)),cursor.getBlob(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_BACKDROP_PATH)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_ADULT)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_OVERVIEW)),cursor.getString(cursor.getColumnIndex(DBContract.UserEntry.COLUMN_RELEASE_DATE))))
                cursor.moveToNext()
            }
        }
        return users
    }

    fun getData(sql: String): Cursor {
        val database = readableDatabase
        return database.rawQuery(sql, null)
    }


    companion object {
        // If you change the database schema, you must increment the database version.
        val DATABASE_VERSION = 1
        val DATABASE_NAME = "FeedReader.db"

        private val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.UserEntry.TABLE_NAME + " (" +
                        DBContract.UserEntry.COLUMN_VOTE_COUNT + " TEXT," +
                        DBContract.UserEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                        DBContract.UserEntry.COLUMN_VIDEO + " TEXT," +
                        DBContract.UserEntry.COLUMN_VOTE_AVERAGE + " TEXT," +
                        DBContract.UserEntry.COLUMN_TITLE + " TEXT," +
                        DBContract.UserEntry.COLUMN_POPULARITY + " TEXT," +
                        DBContract.UserEntry.COLUMN_POSTER_PATH + " BLOD," +
                        DBContract.UserEntry.COLUMN_ORIGINAL_LANGUAGE + " TEXT," +
                        DBContract.UserEntry.COLUMN_ORIGINAL_TITLE + " TEXT," +
                        DBContract.UserEntry.COLUMN_GENRE_IDS + " TEXT," +
                        DBContract.UserEntry.COLUMN_BACKDROP_PATH + " BLOD," +
                        DBContract.UserEntry.COLUMN_ADULT + " TEXT," +
                        DBContract.UserEntry.COLUMN_OVERVIEW + " TEXT," +
                        DBContract.UserEntry.COLUMN_RELEASE_DATE + " TEXT)"

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.UserEntry.TABLE_NAME
    }

}

