package com.ericktijerou.hackernews.data.cache.dao

import androidx.room.*
import com.ericktijerou.hackernews.data.cache.entity.FavoriteNewsEntity

@Dao
interface FavoriteNewsDao {
    @Query("SELECT * FROM Favorite")
    suspend fun getAll(): List<FavoriteNewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorites(vararg favoriteNews: FavoriteNewsEntity)

    @Delete
    suspend fun deleteFavorites(vararg favoriteNews: FavoriteNewsEntity)
}