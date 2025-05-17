package com.example.autn.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "portfolios")
data class PortfolioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val college: String
)