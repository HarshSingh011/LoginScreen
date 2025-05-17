package com.example.autn.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.autn.domain.model.Portfolio
import com.example.autn.domain.usecase.GetPortfolioUseCase
import com.example.autn.domain.usecase.LogoutUseCase
import com.example.autn.domain.usecase.SavePortfolioUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val savePortfolioUseCase: SavePortfolioUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _portfolioState = MutableStateFlow(Portfolio())
    val portfolioState: StateFlow<Portfolio> = _portfolioState.asStateFlow()

    fun loadPortfolio() {
        viewModelScope.launch {
            _portfolioState.value = getPortfolioUseCase()
        }
    }

    fun updateName(name: String) {
        _portfolioState.value = _portfolioState.value.copy(name = name)
    }

    fun updateCollege(college: String) {
        _portfolioState.value = _portfolioState.value.copy(college = college)
    }

    fun addSkill(skill: String) {
        val currentSkills = _portfolioState.value.skills.toMutableList()
        if (skill.isNotEmpty() && !currentSkills.contains(skill)) {
            currentSkills.add(skill)
            _portfolioState.value = _portfolioState.value.copy(skills = currentSkills)
        }
    }

    fun removeSkill(skill: String) {
        val currentSkills = _portfolioState.value.skills.toMutableList()
        currentSkills.remove(skill)
        _portfolioState.value = _portfolioState.value.copy(skills = currentSkills)
    }

    fun savePortfolio() {
        viewModelScope.launch {
            savePortfolioUseCase(_portfolioState.value)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}