package com.studyandroid.musicapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.studyandroid.musicapp.data.enums.GenreEnum
import java.util.*

@Entity(tableName = "albums")
data class Album(
    @ColumnInfo(name = "album_id") @PrimaryKey(autoGenerate = true) val albumId: Long = 0,
    val title: String,
    val genre: GenreEnum,
    @ColumnInfo(name = "number_of_tracks") val numOfTracks: Long,
    @ColumnInfo(name = "release_date") val releaseDate: Date?,
    @ColumnInfo(name = "artist_id") val artistId: Long,
) {
    @Ignore
    var artistName: String = ""
}
