package com.example.culina.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.culina.R
import com.example.culina.ui.theme.AppTheme

@Composable
fun CulinaButtonIcon(text: String, @DrawableRes iconId: Int, onClick: () -> Unit) {

    Button(
        onClick = {
            onClick()
        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(iconId),
                contentDescription = "Icon",
                modifier = Modifier.size(48.dp)
            )
            Text(text)

        }
    }
}

@Composable
fun CulinaButtonPrimary(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(text)
    }
}

@Preview()
@Composable
fun CulinaButtonIconPreview() {
    AppTheme(dynamicColor = false) {

        CulinaButtonIcon("Cooked Today?", R.drawable.baseline_photo_camera_24, {})
    }
}

@Preview
@Composable
fun CulinaButtonPrimaryPreview() {
    AppTheme(dynamicColor = false) {

        CulinaButtonPrimary("Click!") { }
    }
}
