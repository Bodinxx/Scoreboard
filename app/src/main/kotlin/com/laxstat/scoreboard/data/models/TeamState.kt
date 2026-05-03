package com.laxstat.scoreboard.data.models

data class TeamState(
    val id: Long = 0L,
    val name: String = "Team",
    val colorCode: String = "#1565C0",
    val goals: Int = 0,
    val shotsOnGoal: Int = 0
) {
    val savePercent: Float
        get() = if (shotsOnGoal > 0) {
            ((shotsOnGoal - goals).toFloat() / shotsOnGoal) * 100f
        } else 0f
}
