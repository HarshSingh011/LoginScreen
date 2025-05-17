package com.example.autn.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.autn.data.db.dao.PortfolioDao
import com.example.autn.data.db.dao.SkillDao
import com.example.autn.data.db.entity.PortfolioEntity
import com.example.autn.data.db.entity.SkillEntity

@Database(entities = [PortfolioEntity::class, SkillEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun portfolioDao(): PortfolioDao
    abstract fun skillDao(): SkillDao
}