package com.example.autn.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.autn.data.db.entity.SkillEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkill(skill: SkillEntity): Long

    @Query("SELECT * FROM skills WHERE portfolioId = :portfolioId ORDER BY `order`")
    suspend fun getSkillsForPortfolio(portfolioId: Long): List<SkillEntity>

    @Query("SELECT * FROM skills WHERE portfolioId = :portfolioId ORDER BY `order`")
    fun observeSkillsForPortfolio(portfolioId: Long): Flow<List<SkillEntity>>

    @Query("DELETE FROM skills")
    suspend fun deleteAllSkills()
}