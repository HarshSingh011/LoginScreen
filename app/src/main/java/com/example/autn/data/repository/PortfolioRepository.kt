package com.example.autn.domain.repository

import com.example.autn.domain.model.Portfolio
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {
    suspend fun savePortfolio(portfolio: Portfolio)
    suspend fun getPortfolio(): Portfolio
    fun observePortfolio(): Flow<Portfolio>
}