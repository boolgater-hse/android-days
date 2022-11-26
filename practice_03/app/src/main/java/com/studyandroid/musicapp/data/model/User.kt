package com.studyandroid.musicapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @ColumnInfo(name = "user_id") @PrimaryKey(autoGenerate = true) val userId: Long = 0,
    val name: String = "",
    val email: String,
    val password: String,
)
