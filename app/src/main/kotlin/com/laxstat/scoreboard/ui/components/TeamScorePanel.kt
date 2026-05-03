package com.laxstat.scoreboard.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.laxstat.scoreboard.data.models.TeamState

@Composable
fun TeamScorePanel(
    teamState: TeamState,
    showShotsOnGoal: Boolean,
    showSavePercent: Boolean,
    showDecrementButtons: Boolean,
    onGoalIncrement: () -> Unit,
    onGoalDecrement: () -> Unit,
    onShotIncrement: () -> Unit,
    onShotDecrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    val teamColor = try {
        Color(android.graphics.Color.parseColor(teamState.colorCode))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primary
    }

    Column(
        modifier = modifier
            .background(teamColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Team Name
        Text(
            text = teamState.name,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = teamColor
            ),
            textAlign = TextAlign.Center,
            maxLines = 1
        )

        // Goals Section
        Text(
            text = "GOALS",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        // Goal swipe detector
        var swipeAccum by remember { mutableFloatStateOf(0f) }
        Box(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = { swipeAccum = 0f },
                        onDragCancel = { swipeAccum = 0f }
                    ) { change, dragAmount ->
                        change.consume()
                        swipeAccum += dragAmount.y
                        if (swipeAccum > 80f) {
                            onGoalDecrement()
                            swipeAccum = 0f
                        }
                    }
                }
        ) {
            Text(
                text = teamState.goals.toString(),
                fontSize = 72.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showDecrementButtons) {
                FilledTonalIconButton(onClick = onGoalDecrement) {
                    Icon(Icons.Default.Remove, contentDescription = "Goal -1")
                }
            }
            Button(
                onClick = onGoalIncrement,
                colors = ButtonDefaults.buttonColors(containerColor = teamColor)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Goal +1")
                Spacer(Modifier.width(4.dp))
                Text("+1 Goal")
            }
        }

        if (showShotsOnGoal) {
            HorizontalDivider(color = teamColor.copy(alpha = 0.3f))
            Text(
                text = "SHOTS ON GOAL",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = teamState.shotsOnGoal.toString(),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showDecrementButtons) {
                    FilledTonalIconButton(onClick = onShotDecrement) {
                        Icon(Icons.Default.Remove, contentDescription = "Shot -1")
                    }
                }
                Button(
                    onClick = onShotIncrement,
                    colors = ButtonDefaults.buttonColors(containerColor = teamColor)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Shot +1")
                    Spacer(Modifier.width(4.dp))
                    Text("+1 Shot")
                }
            }
        }

        if (showSavePercent && showShotsOnGoal) {
            HorizontalDivider(color = teamColor.copy(alpha = 0.3f))
            Text(
                text = "SAVE %",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = "%.1f%%".format(teamState.savePercent),
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}
