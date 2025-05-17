package com.example.autn.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "skills",
    foreignKeys = [
        ForeignKey(
            entity = PortfolioEntity::class,
            parentColumns = ["id"],
            childColumns = ["portfolioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("portfolioId")]
)
data class SkillEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val portfolioId: Long,
    val skill: String,
    val order: Int
)