package com.example.culina.dailyCooking

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.culina.R
import com.example.culina.common.BackgroundPanel
import com.example.culina.common.CulinaButtonPrimary

@Composable
fun DailyCookingOverview(innerPadding: PaddingValues) {
    var photoSelectActive by rememberSaveable { mutableStateOf(false) }
    var showUploadMealScreen by rememberSaveable { mutableStateOf(false) }
    var capturedImageUri by rememberSaveable {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    fun showPhotoSelectScreen() {
        photoSelectActive = true
    }

    fun updateImageUri(newUri: Uri) {
        capturedImageUri = newUri
        showUploadMealScreen = true
        photoSelectActive = false
    }

    BackgroundPanel(innerPadding) {
        if (photoSelectActive) {
            PhotoSelectScreen({ uri -> updateImageUri(uri) })
        } else if (showUploadMealScreen) {
            UploadMealScreen(capturedImageUri, emptySet())
        } else {
            Row(
                Modifier
                    .padding(20.dp)
                    .fillMaxSize(), horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CulinaButtonPrimary(
                        "Cooked Today?",
                        R.drawable.baseline_photo_camera_24,
                        ::showPhotoSelectScreen
                    )
                }
            }
        }
    }
}
