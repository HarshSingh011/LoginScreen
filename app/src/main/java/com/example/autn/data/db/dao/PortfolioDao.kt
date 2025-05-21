package com.example.autn.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.autn.data.db.entity.PortfolioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PortfolioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPortfolio(portfolio: PortfolioEntity): Long

    @Query("SELECT * FROM portfolios WHERE id = 1")
    suspend fun getPortfolio(): PortfolioEntity?

    @Query("SELECT * FROM portfolios WHERE id = 1")
    fun observePortfolio(): Flow<PortfolioEntity?>
}