package com.ericktijerou.hackernews.data.cache.system

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ericktijerou.hackernews.data.cache.dao.NewsDao
import com.ericktijerou.hackernews.data.cache.entity.NewsEntity

@Database(entities = [NewsEntity::class], version = 1, exportSchema = false)
abstract class SystemDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        fun newInstance(context: Context): SystemDatabase {
            return Room.databaseBuilder(context, SystemDatabase::class.java, "news-system.db").build()
        }
    }
}