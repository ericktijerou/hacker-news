package com.ericktijerou.hackernews.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ericktijerou.hackernews.data.cache.entity.NewsEntity

@Dao
interface NewsDao {

    @Query("SELECT * FROM News")
    suspend fun getAll(): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newsList: List<NewsEntity>)

    @Query("SELECT * FROM News WHERE id = :newsId")
    suspend fun getNewsById(newsId: Long): NewsEntity

    @Query("DELETE FROM News")
    suspend fun deleteAll()
}