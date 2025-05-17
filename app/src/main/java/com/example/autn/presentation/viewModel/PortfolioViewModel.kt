package com.example.autn.presentation.viewmodel

import android.util.Log
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

    private val _skillInputText = MutableStateFlow("")
    val skillInputText = _skillInputText.asStateFlow()

    private var dataLoaded = false

    private val predefinedSkills = listOf(
        "Web Development", "Android Development", "iOS Development",
        "Machine Learning", "Data Science", "UI/UX Design",
        "JavaScript", "Python", "Kotlin", "Java", "Swift",
        "React", "Flutter", "Node.js", "Firebase", "AWS",
        "Database Management", "DevOps", "Git", "Agile Methodology"
    )

    private val _skillSuggestions = MutableStateFlow<List<String>>(emptyList())
    val skillSuggestions = _skillSuggestions.asStateFlow()

    fun loadPortfolio() {
        if (dataLoaded) return

        viewModelScope.launch {
            try {
                val portfolio = getPortfolioUseCase()

                if (portfolio.name.isNotEmpty() || portfolio.college.isNotEmpty() || portfolio.skills.isNotEmpty()) {
                    _portfolioState.value = portfolio
                    Log.d("PortfolioViewModel", "Loaded portfolio: ${portfolio.name}, ${portfolio.college}, ${portfolio.skills.size} skills")
                }

                dataLoaded = true
            } catch (e: Exception) {
                Log.e("PortfolioViewModel", "Error loading portfolio", e)
                dataLoaded = true
            }
        }
    }

    fun updateName(name: String) {
        _portfolioState.value = _portfolioState.value.copy(name = name)
    }

    fun updateCollege(college: String) {
        _portfolioState.value = _portfolioState.value.copy(college = college)
    }

    fun updateSkillInput(input: String) {
        _skillInputText.value = input
        if (input.isNotEmpty()) {
            _skillSuggestions.value = predefinedSkills.filter {
                it.lowercase().contains(input.lowercase())
            }
        } else {
            _skillSuggestions.value = emptyList()
        }
    }

    fun clearSkillInput() {
        _skillInputText.value = ""
        _skillSuggestions.value = emptyList()
    }

    fun addSkill(skill: String) {
        val currentSkills = _portfolioState.value.skills.toMutableList()
        if (skill.isNotEmpty() && !currentSkills.contains(skill)) {
            currentSkills.add(skill)
            _portfolioState.value = _portfolioState.value.copy(skills = currentSkills)
        }
        clearSkillInput()
    }

    fun removeSkill(skill: String) {
        val currentSkills = _portfolioState.value.skills.toMutableList()
        currentSkills.remove(skill)
        _portfolioState.value = _portfolioState.value.copy(skills = currentSkills)
    }

    fun savePortfolio() {
        viewModelScope.launch {
            try {
                savePortfolioUseCase(_portfolioState.value)
                Log.d("PortfolioViewModel", "Saved portfolio: ${_portfolioState.value.name}, ${_portfolioState.value.college}, ${_portfolioState.value.skills.size} skills")
            } catch (e: Exception) {
                Log.e("PortfolioViewModel", "Error saving portfolio", e)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}