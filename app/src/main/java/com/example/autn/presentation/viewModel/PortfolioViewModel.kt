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
import org.json.JSONArray
import java.net.URL

data class Quote(val text: String = "", val author: String = "")

class PortfolioViewModel(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val savePortfolioUseCase: SavePortfolioUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    private val _portfolioState = MutableStateFlow(Portfolio())
    val portfolioState: StateFlow<Portfolio> = _portfolioState.asStateFlow()

    private val _skillInputText = MutableStateFlow("")
    val skillInputText = _skillInputText.asStateFlow()

    private val _quoteState = MutableStateFlow(Quote())
    val quoteState: StateFlow<Quote> = _quoteState.asStateFlow()

    private val _isQuoteLoading = MutableStateFlow(false)
    val isQuoteLoading: StateFlow<Boolean> = _isQuoteLoading.asStateFlow()

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
        viewModelScope.launch {
            try {
                val portfolio = getPortfolioUseCase()
                _portfolioState.value = portfolio
                Log.d("PortfolioViewModel", "Loaded portfolio: ${portfolio.name}, ${portfolio.college}, ${portfolio.skills.size} skills")
            } catch (e: Exception) {
                Log.e("PortfolioViewModel", "Error loading portfolio", e)
            }
        }
    }

    fun fetchQuote() {
        viewModelScope.launch {
            _isQuoteLoading.value = true
            try {
                val response = kotlin.runCatching {
                    val jsonStr = URL("https://zenquotes.io/api/today").readText()
                    val jsonArray = JSONArray(jsonStr)
                    val jsonObject = jsonArray.getJSONObject(0)
                    val text = jsonObject.getString("q")
                    val author = jsonObject.getString("a")
                    Quote(text, author)
                }.getOrElse {
                    Log.e("PortfolioViewModel", "Error fetching quote", it)
                    Quote(
                        "Sometimes you have to lose all you have to find out who you truly are.",
                        "Roy T. Bennett"
                    )
                }

                _quoteState.value = response
                Log.d("PortfolioViewModel", "Fetched quote: ${response.text} - ${response.author}")
            } finally {
                _isQuoteLoading.value = false
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