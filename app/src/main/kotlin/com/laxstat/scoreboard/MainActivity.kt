package com.laxstat.scoreboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.laxstat.scoreboard.navigation.AppNavigation
import com.laxstat.scoreboard.ui.theme.LaxStatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaxStatTheme {
                AppNavigation()
            }
        }
    }
}
