package com.example.culina.common

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.culina.ui.theme.AppTheme

@Composable
fun CulinaButtonPrimary() {

    Button(
        onClick = {
            println("clicked!")
        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Cooked Today?")
    }
}

@Preview()
@Composable
fun CulinaButtonPrimaryPreview() {
    AppTheme(dynamicColor = false) {

        CulinaButtonPrimary()
    }
}
