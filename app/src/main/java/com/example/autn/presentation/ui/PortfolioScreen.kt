package com.example.autn.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.autn.presentation.viewmodel.PortfolioViewModel

@Composable
fun PortfolioScreen(
    navController: NavController,
    viewModel: PortfolioViewModel
) {
    val portfolioState by viewModel.portfolioState.collectAsState()
    val skillInputText by viewModel.skillInputText.collectAsState()
    val skillSuggestions by viewModel.skillSuggestions.collectAsState()
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }

    var nameError by remember { mutableStateOf<String?>(null) }
    var collegeError by remember { mutableStateOf<String?>(null) }
    var skillsError by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(16.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                focusManager.clearFocus()
            }
    ) {
        Text(
            text = "Edit Portfolio",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = portfolioState.name,
            onValueChange = {
                viewModel.updateName(it)
                nameError = null
            },
            label = { Text("Name") },
            singleLine = true,
            isError = nameError != null,
            supportingText = {
                nameError?.let { Text(it) }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = portfolioState.college,
            onValueChange = {
                viewModel.updateCollege(it)
                collegeError = null
            },
            label = { Text("College") },
            singleLine = true,
            isError = collegeError != null,
            supportingText = {
                collegeError?.let { Text(it) }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Text(
            text = "Skills",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
        )

        OutlinedTextField(
            value = skillInputText,
            onValueChange = {
                viewModel.updateSkillInput(it)
                if (portfolioState.skills.size >= 3) {
                    skillsError = null
                }
            },
            label = { Text("Add Skill") },
            singleLine = true,
            isError = skillsError != null,
            supportingText = {
                skillsError?.let { Text(it) }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (skillInputText.isNotEmpty()) {
                        viewModel.addSkill(skillInputText)
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    if (skillInputText.isNotEmpty()) {
                        viewModel.addSkill(skillInputText)
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Skill")
                }
            }
        )

        if (skillSuggestions.isNotEmpty()) {
            Text(
                text = "Suggestions:",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )

            LazyRow(
                modifier = Modifier.padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(skillSuggestions) { suggestion ->
                    SuggestionChip(
                        onClick = {
                            viewModel.addSkill(suggestion)
                            focusManager.clearFocus()
                        },
                        label = { Text(suggestion) }
                    )
                }
            }
        }

        Text(
            text = "Current Skills:",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(portfolioState.skills) { skill ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(skill)
                        IconButton(onClick = { viewModel.removeSkill(skill) }) {
                            Icon(Icons.Default.Clear, contentDescription = "Remove Skill")
                        }
                    }
                }
            }
        }

        Button(
            onClick = {
                var isValid = true

                if (portfolioState.name.isBlank()) {
                    nameError = "Name is required"
                    isValid = false
                }

                if (portfolioState.college.isBlank()) {
                    collegeError = "College is required"
                    isValid = false
                }

                if (portfolioState.skills.size < 3) {
                    skillsError = "At least 3 skills are required"
                    isValid = false
                }

                if (isValid) {
                    viewModel.savePortfolio()
                    viewModel.loadPortfolio()
                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}