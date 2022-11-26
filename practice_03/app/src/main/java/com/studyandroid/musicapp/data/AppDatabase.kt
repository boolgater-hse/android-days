package com.studyandroid.musicapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.studyandroid.musicapp.data.converter.Converters
import com.studyandroid.musicapp.data.dao.*
import com.studyandroid.musicapp.data.model.*

@Database(
    entities = [Track::class, Library::class, Artist::class, Album::class, User::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val temp = instance
            if (temp != null) {
                return temp
            }
            synchronized(this) {
                val builder = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "musicapp.db"
                )
                    .createFromAsset("database/sample.db")
                    .allowMainThreadQueries() // Used once on LibraryRepository.kt:28
                    .build()
                instance = builder
                return builder
            }
        }
    }

    abstract fun usersDao(): UsersDao

    abstract fun libraryDao(): LibraryDao

    abstract fun artistsDao(): ArtistsDao

    abstract fun albumsDao(): AlbumsDao

    abstract fun tracksDao(): TracksDao
}
