package com.example.autn.domain.usecase

import com.example.autn.domain.model.Portfolio
import com.example.autn.domain.repository.PortfolioRepository
import kotlinx.coroutines.flow.Flow

class GetPortfolioUseCase(private val repository: PortfolioRepository) {
    suspend operator fun invoke(): Portfolio {
        return repository.getPortfolio()
    }

    fun observePortfolio(): Flow<Portfolio> {
        return repository.observePortfolio()
    }
}