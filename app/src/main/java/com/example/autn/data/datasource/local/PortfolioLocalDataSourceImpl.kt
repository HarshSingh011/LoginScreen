package com.example.autn.data.datasource.local

import android.content.Context
import androidx.room.Room
import com.example.autn.data.db.AppDatabase
import com.example.autn.data.db.entity.PortfolioEntity
import com.example.autn.data.db.entity.SkillEntity
import com.example.autn.domain.model.Portfolio
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PortfolioLocalDataSourceImpl(context: Context) : PortfolioLocalDataSource {

    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "portfolio-db"
    ).build()

    override suspend fun savePortfolio(portfolio: Portfolio) = withContext(Dispatchers.IO) {
        // First save the portfolio
        val portfolioId = db.portfolioDao().insertPortfolio(
            PortfolioEntity(
                id = 1, // Always use ID 1 for simplicity
                name = portfolio.name,
                college = portfolio.college
            )
        )

        // Delete existing skills
        db.skillDao().deleteAllSkills()

        // Insert new skills
        portfolio.skills.forEachIndexed { index, skill ->
            db.skillDao().insertSkill(
                SkillEntity(
                    id = 0, // Auto-generate ID
                    portfolioId = portfolioId,
                    skill = skill,
                    order = index
                )
            )
        }
    }

    override suspend fun getPortfolio(): Portfolio = withContext(Dispatchers.IO) {
        val portfolioEntity = db.portfolioDao().getPortfolio() ?: PortfolioEntity(id = 0, name = "", college = "")
        val skills = db.skillDao().getSkillsForPortfolio(portfolioEntity.id).map { it.skill }

        Portfolio(
            name = portfolioEntity.name,
            college = portfolioEntity.college,
            skills = skills
        )
    }

    override fun observePortfolio(): Flow<Portfolio> {
        return db.portfolioDao().observePortfolio().map { portfolioEntity ->
            if (portfolioEntity == null) {
                Portfolio()
            } else {
                val skills = db.skillDao().observeSkillsForPortfolio(portfolioEntity.id).map { skillEntities ->
                    skillEntities.map { it.skill }
                }

                Portfolio(
                    name = portfolioEntity.name,
                    college = portfolioEntity.college,
                    skills = skills.firstOrNull() ?: emptyList()
                )
            }
        }
    }
}