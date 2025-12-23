package com.example.string

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.example.string.logic.StreakViewModel
import com.example.string.ui.theme.StringTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.string.ui.theme.HomeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StringTheme {
                StringApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun StringApp() {
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var goalText by rememberSaveable { mutableStateOf("My streak") }
    val viewModel: StreakViewModel = viewModel()
    val streak by viewModel.streak.collectAsState()

    val (panelColor, textColor, iconColor) = if (currentDestination == AppDestinations.HOME) {
        Triple(0xFFEC9C40, 0xFF3D1611, 0xFF3D1611)  // Home panel: orange bg, brown text/icon
    } else {
        Triple(0xFF3D1611, 0xFFEC9C40, 0xFFEC9C40)  // Game panel: brown bg, orange text/icon
    }


    NavigationSuiteScaffold(
        containerColor = Color(0xFF3D1611),
        contentColor = Color(0xFFEC9C40),
        navigationSuiteItems = {
            AppDestinations.entries.forEach {
                item(
                    icon = {
                        Icon(
                            it.icon,
                            contentDescription = it.label,
                            tint = Color(0xFFEC9C40)
                        )
                    },
                    label = { Text(it.label, color = Color(0xFFEC9C40)) },
                    selected = it == currentDestination,
                    onClick = { currentDestination = it }
                )
            }
        }
    ) {
        Scaffold( topBar = {

            if (currentDestination == AppDestinations.HOME) {
                AppTopPanel(
                    number = streak,
                    centerText = goalText,
                    onSettingsClick = {
                        showSettingsDialog = true
                    },
                    textColor = 0xFF3D1611,
                    iconColor = 0xFF3D1611
                )

            }
        },){ padding ->
            when (currentDestination) {
                AppDestinations.HOME ->
                    HomeScreen(
                        modifier = Modifier.padding(padding),
                        viewModel = viewModel
                    )

                AppDestinations.GAME ->
                    Greeting("Game Screen", Modifier.padding(padding))
            }
        }
    }
    if (showSettingsDialog) {
        SettingsDialog(
            goalText = goalText,
            onGoalChange = { goalText = it },
            onDismiss = { showSettingsDialog = false }
        )
    }
}

enum class AppDestinations(
    val label: String,
    val icon: ImageVector
) {
    GAME("Game", Icons.Filled.Star),
    HOME("Streak", Icons.Filled.CheckCircle)
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}


@Composable
fun AppTopPanel(
    number: Int,
    centerText: String,
    onSettingsClick: () -> Unit,
    textColor: Long,
    iconColor: Long
) {
    Surface(
        color = Color(0xFFEC9C40)
    ){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = Color(textColor),
            modifier = Modifier

        )

        Text(
            text = centerText,
            modifier = Modifier.weight(1f),
            color = Color(textColor),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        IconButton(onClick = onSettingsClick) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                tint = Color(iconColor)
            )
        }
    }
    }
}

@Composable
fun SettingsDialog(
    goalText: String,
    onGoalChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Settings")
        },
        text = {
            Column {
                // Language label
                Text(
                    text = "Language: English",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Goal label + textfield
                Text(text = "Your goal:")
                OutlinedTextField(
                    value = goalText,
                    onValueChange = onGoalChange,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Back")
            }
        }
    )
}


