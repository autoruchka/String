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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.string.R
import androidx.compose.ui.unit.dp


@Composable
fun HomeScreen(modifier: Modifier = Modifier, viewModel: StreakViewModel) {
    val context = LocalContext.current
    val streak by viewModel.streak.collectAsState()

    // Initialize storage once
    LaunchedEffect(Unit) {
        try {
            viewModel.initialize(context)
        } catch (t: Throwable) {
            Log.e("HomeScreen", "Failed to initialize viewModel: ${t.message}", t)
        }
    }



    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onDoubleTap = { viewModel.onStreakTap() })
            }
    ) {
        Image(
            painter = painterResource(R.drawable.background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Image(
            painter = painterResource(R.drawable.masckot),
            contentDescription = "Center image",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .fillMaxHeight(0.75f),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Current streak: $streak",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 120.dp),
            color = Color.White
        )
    }
}