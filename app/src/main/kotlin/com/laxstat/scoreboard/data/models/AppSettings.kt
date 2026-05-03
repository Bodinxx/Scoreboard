package com.laxstat.scoreboard.data.models

data class AppSettings(
    val showShotsOnGoal: Boolean = true,
    val showSavePercent: Boolean = true,
    val showDecrementButtons: Boolean = true,
    val homeTeamName: String = "Home",
    val awayTeamName: String = "Away",
    val homeTeamColor: String = "#1565C0",
    val awayTeamColor: String = "#B71C1C",
    val penaltyTypes: List<String> = listOf(
        "Slashing", "Tripping", "Cross-checking", "Holding", "Interference"
    )
)
