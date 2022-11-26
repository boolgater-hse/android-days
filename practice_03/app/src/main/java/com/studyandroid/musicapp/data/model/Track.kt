package com.studyandroid.musicapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.studyandroid.musicapp.data.enums.MediaTypeEnum

@Entity(tableName = "tracks")
data class Track(
    @ColumnInfo(name = "track_id") @PrimaryKey(autoGenerate = true) val trackId: Long = 0,
    @ColumnInfo(name = "track_name") val trackName: String,
    @ColumnInfo(name = "num_of_track") val numOfTrack: Long,
    @ColumnInfo(name = "media_type") val mediaType: MediaTypeEnum,
    @ColumnInfo(name = "bit_depth") val bitDepth: Int?,
    @ColumnInfo(name = "sample_rate") val sampleRate: Double?,
    @ColumnInfo(name = "album_id") val albumId: Long,
    @ColumnInfo(name = "artist_id") val artistId: Long,
) {
    @Ignore
    var artistName: String = ""

    @Ignore
    var albumName: String = ""
}
