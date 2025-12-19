package com.example.string.ui.theme
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.example.string.logic.StreakViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.string.R
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val viewModel: StreakViewModel = viewModel()
    val context = LocalContext.current

    // Initialize storage once
    LaunchedEffect(Unit) {
        try {
            viewModel.initialize(context)
        } catch (t: Throwable) {
            Log.e("HomeScreen", "Failed to initialize viewModel: ${t.message}", t)
        }
    }

    val streak by viewModel.streak.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { viewModel.onStreakTap() })
            }
    ) {
        // Try to load background; fallback to colored Box if fails
        val bgPainter = runCatching { painterResource(id = R.drawable.background) }.getOrNull()
        if (bgPainter != null) {
            Image(painter = bgPainter, contentDescription = null, modifier = Modifier.matchParentSize(), contentScale = ContentScale.Crop)
        } else {
            // fallback background if resource missing or invalid
            Box(modifier = Modifier.matchParentSize(), contentAlignment = Alignment.Center) {
                // simple colored background
                Surface(modifier = Modifier.matchParentSize()) { /* default material background */ }
            }
            Log.w("HomeScreen", "Background drawable not found: R.drawable.background")
        }

        // Center animated image (safely)
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val centerPainter = runCatching { painterResource(id = R.drawable.masckot) }.getOrNull()
            if (centerPainter != null) {
                Image(
                    painter = centerPainter,
                    contentDescription = "Center image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f),   // takes 75% of screen height
                    contentScale = ContentScale.Fit
                )
            } else {
                Log.w("HomeScreen", "Center drawable not found: R.drawable.masckot")
                Text("Image", style = MaterialTheme.typography.headlineLarge)
            }
        }

        // Small overlay text: current streak
        Text(
            text = "Current streak: $streak",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 110.dp),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
