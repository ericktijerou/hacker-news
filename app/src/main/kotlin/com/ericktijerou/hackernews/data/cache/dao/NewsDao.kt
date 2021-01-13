package com.ericktijerou.hackernews.data.cache.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ericktijerou.hackernews.data.cache.entity.NewsEntity

@Dao
interface NewsDao {

    @Query("SELECT * FROM News ORDER BY date DESC")
    fun getAll(): DataSource.Factory<Int, NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newsList: List<NewsEntity>)

    @Query("DELETE FROM News")
    suspend fun deleteAll()

    @Query("DELETE FROM News WHERE id = :newsId")
    suspend fun deleteById(newsId: String)
}