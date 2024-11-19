package com.example.culina.dailyCooking

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.example.culina.BuildConfig
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PhotoSelectScreen(updateImageUri: (Uri) -> Unit) {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateString = LocalDate.now().format(formatter)
    val imageUri = LocalContext.current.createTempPictureUri(fileName = "meal_${dateString}.jpg")
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { captured ->
            if (captured) {
                updateImageUri(imageUri)
            } else {
                updateImageUri(Uri.EMPTY)
            }
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                cameraLauncher.launch(imageUri)
            }
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}

fun Context.createTempPictureUri(
    provider: String = "${BuildConfig.APPLICATION_ID}.provider",
    fileName: String = "picture_${System.currentTimeMillis()}",
    fileExtension: String = ".png"
): Uri {
    val tempFile = File.createTempFile(
        fileName, fileExtension, cacheDir
    ).apply {
        createNewFile()
    }

    return FileProvider.getUriForFile(applicationContext, provider, tempFile)
}