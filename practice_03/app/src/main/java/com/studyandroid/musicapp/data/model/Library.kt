package com.studyandroid.musicapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "library",
    indices = [Index(value = ["lib_owner_id", "track_id"], unique = true)]
)
data class Library(
    @ColumnInfo(name = "library_id") @PrimaryKey(autoGenerate = true) val playlistId: Long = 0,
    @ColumnInfo(name = "lib_owner_id") val libraryOwnerId: Long,
    @ColumnInfo(name = "track_id") val trackId: Long,
)
