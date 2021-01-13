package com.ericktijerou.hackernews.data.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ericktijerou.hackernews.data.cache.entity.BlockNewsEntity

@Dao
interface BlockNewsDao {
    @Query("SELECT * FROM Block")
    suspend fun getAll(): List<BlockNewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlocks(vararg blockNews: BlockNewsEntity)
}