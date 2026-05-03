package com.laxstat.scoreboard.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.laxstat.scoreboard.data.models.AppSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    fun setShowShotsOnGoal(show: Boolean) = _settings.update { it.copy(showShotsOnGoal = show) }
    fun setShowSavePercent(show: Boolean) = _settings.update { it.copy(showSavePercent = show) }
    fun setShowDecrementButtons(show: Boolean) = _settings.update { it.copy(showDecrementButtons = show) }
    fun setHomeTeamName(name: String) = _settings.update { it.copy(homeTeamName = name) }
    fun setAwayTeamName(name: String) = _settings.update { it.copy(awayTeamName = name) }
    fun setHomeTeamColor(color: String) = _settings.update { it.copy(homeTeamColor = color) }
    fun setAwayTeamColor(color: String) = _settings.update { it.copy(awayTeamColor = color) }
    fun addPenaltyType(type: String) = _settings.update { it.copy(penaltyTypes = it.penaltyTypes + type) }
    fun removePenaltyType(type: String) = _settings.update { it.copy(penaltyTypes = it.penaltyTypes - type) }
    fun getSettings(): AppSettings = _settings.value
}
