package com.example.autn.domain.usecase

import com.example.autn.domain.model.Portfolio
import com.example.autn.domain.repository.PortfolioRepository

class SavePortfolioUseCase(private val repository: PortfolioRepository) {
    suspend operator fun invoke(portfolio: Portfolio) {
        repository.savePortfolio(portfolio)
    }
}