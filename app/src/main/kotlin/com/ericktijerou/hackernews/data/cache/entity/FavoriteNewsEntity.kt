package com.ericktijerou.hackernews.data.cache.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class FavoriteNewsEntity(
    @PrimaryKey var id: String
)