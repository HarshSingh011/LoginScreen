package com.example.autn.data.datasource.local

import com.example.autn.domain.model.Portfolio
import kotlinx.coroutines.flow.Flow

interface PortfolioLocalDataSource {
    suspend fun savePortfolio(portfolio: Portfolio)
    suspend fun getPortfolio(): Portfolio
    fun observePortfolio(): Flow<Portfolio>
}