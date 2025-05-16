package com.example.autn.domain.model

data class Portfolio(
    val name: String = "",
    val college: String = "",
    val skills: List<String> = emptyList()
)