package com.ericktijerou.hackernews.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Block")
data class BlockNewsEntity(
    @PrimaryKey var id: String
)