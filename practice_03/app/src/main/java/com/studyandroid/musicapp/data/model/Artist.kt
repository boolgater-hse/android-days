package com.studyandroid.musicapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artists",
    indices = [Index(value = ["name"], unique = true)]
)
data class Artist(
    @ColumnInfo(name = "artist_id") @PrimaryKey(autoGenerate = true) val artistId: Long = 0,
    @ColumnInfo(name = "name") val name: String,
)
