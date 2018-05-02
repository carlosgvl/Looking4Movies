package com.example.carlosvivas.looking4movies.Controller

import android.provider.BaseColumns

/**
 * Created by carlosvivas on 5/1/18.
 */
object DBContract {

    /* Inner class that defines the table contents */
    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "movies"
            val COLUMN_VOTE_COUNT = "vote_count"
            val COLUMN_ID = "id"
            val COLUMN_VIDEO = "video"
            val COLUMN_VOTE_AVERAGE = "vote_average"
            val COLUMN_TITLE = "title"
            val COLUMN_POPULARITY = "popularity"
            val COLUMN_POSTER_PATH = "poster_path"
            val COLUMN_ORIGINAL_LANGUAGE = "original_language"
            val COLUMN_ORIGINAL_TITLE = "original_title"
            val COLUMN_GENRE_IDS = "genre_ids"
            val COLUMN_BACKDROP_PATH = "backdrop_path"
            val COLUMN_ADULT = "adult"
            val COLUMN_OVERVIEW = "overview"
            val COLUMN_RELEASE_DATE = "release_date"
        }
    }
}


