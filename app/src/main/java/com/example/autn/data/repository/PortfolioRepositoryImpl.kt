package com.example.autn.data.repository

import com.example.autn.data.datasource.local.PortfolioLocalDataSource
import com.example.autn.domain.model.Portfolio
import com.example.autn.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow

class PortfolioRepositoryImpl(
    private val localDataSource: PortfolioLocalDataSource
) : PortfolioRepository {

    override suspend fun savePortfolio(portfolio: Portfolio) {
        localDataSource.savePortfolio(portfolio)
    }

    override suspend fun getPortfolio(): Portfolio {
        return localDataSource.getPortfolio()
    }

    override fun observePortfolio(): Flow<Portfolio> {
        return localDataSource.observePortfolio()
    }
}