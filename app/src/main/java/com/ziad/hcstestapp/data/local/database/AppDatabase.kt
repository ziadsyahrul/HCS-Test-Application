package com.ziad.hcstestapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ziad.hcstestapp.data.local.dao.UserDao
import com.ziad.hcstestapp.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}