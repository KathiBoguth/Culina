package com.example.culina.dailyCooking

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.culina.R
import com.example.culina.common.CulinaButtonPrimary
import com.example.culina.ui.theme.AppTheme
import java.io.File
import kotlin.math.roundToInt

@Composable
fun MealPhoto(imageUri: Uri) {

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUri)
            .placeholder(R.drawable.meal)
            .fallback(R.drawable.meal)
            .error(R.drawable.meal)
            .build(),
        contentDescription = "Daily Meal",
        modifier = Modifier.Companion
            .padding(4.dp)
            .size(150.dp)
            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clip(CircleShape),
        contentScale = ContentScale.Companion.Crop,
    )
}

@Composable
fun MealPhotoBitmap(bitmap: Bitmap) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "Daily Meal",
        modifier = Modifier.Companion
            .padding(4.dp)
            .size(150.dp)
            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clip(CircleShape),
        contentScale = ContentScale.Companion.Crop,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadMealScreen(imageUri: Uri) {
    val viewModel: DailyCookingViewModel = hiltViewModel()

    val ingredientList by viewModel.ingredients.collectAsState(emptySet())
    val sliderPosition: Int by viewModel.rating.collectAsState(0)
    val title by viewModel.name.collectAsState("")
    val imageBitmap by viewModel.imageBitmap.collectAsState(null)
    var ingredient by rememberSaveable { mutableStateOf("") }

    val context = LocalContext.current

    fun addIngredient() {
        viewModel.onIngredientsChange(ingredientList.plusElement(ingredient))
        ingredient = ""
    }

    fun deleteIngredient(ingredient: String) {
        viewModel.onIngredientsChange(ingredientList.minusElement(ingredient))

    }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.Companion.CenterHorizontally
    ) {
        if (imageBitmap == null) {
            MealPhoto(imageUri)
        } else {
            MealPhotoBitmap(imageBitmap!!)
        }
        TextField(
            value = title,
            onValueChange = { it -> viewModel.onNameChange(it) },
            label = { Text("Title") }
        )
        TextField(
            value = ingredient,
            onValueChange = { it -> ingredient = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { addIngredient() }
            ),
            label = { Text("Ingredients") },
            trailingIcon = { AddIngredientButton(::addIngredient) }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ingredientList.forEach { ingredient ->
                InputChip(
                    selected = true,
                    label = { Text(ingredient) },
                    onClick = {},
                    trailingIcon = { DeleteIngredientButton { deleteIngredient(ingredient) } },
                    colors = InputChipDefaults.inputChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.secondary,
                        selectedLabelColor = MaterialTheme.colorScheme.onSecondary

                    )
                )
            }
        }
        Row(
            Modifier.Companion
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                "Rate yourself!",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Row(Modifier.Companion.width(240.dp)) {
            Slider(sliderPosition.toFloat(),
                onValueChange = { viewModel.onRatingChange(it.roundToInt()) },
                valueRange = 0f..5f,
                steps = 4,
                track = { sliderState -> RatingTrack(sliderState.value.roundToInt()) },
                thumb = {}
            )
        }
        CulinaButtonPrimary("Save & Upload") {
            val imageByteArray = context.contentResolver.openInputStream(imageUri)
                ?.use { it.buffered().readBytes() }
            if (imageByteArray != null) {
                viewModel.onSaveDailyCooking(
                    imageName = File(imageUri.path ?: "test.png").name,
                    image = imageByteArray
                )

            }
        }
    }
}

@Composable
fun RatingTrack(sliderPosition: Int) {
    Row {
        for (i in 1..sliderPosition) {
            Image(
                painter = painterResource(R.drawable.baseline_star_24),
                colorFilter = ColorFilter.Companion.tint(MaterialTheme.colorScheme.secondary),
                contentDescription = "rating",
                modifier = Modifier.size(48.dp)
            )

        }
        for (i in 1..5 - sliderPosition) {
            Image(
                painter = painterResource(R.drawable.baseline_star_outline_24),
                colorFilter = ColorFilter.Companion.tint(MaterialTheme.colorScheme.secondary),
                contentDescription = "rating",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

@Composable
fun AddIngredientButton(addIngredient: () -> Unit) {
    Icon(
        painterResource(R.drawable.baseline_add_24),
        contentDescription = "add",
        modifier = Modifier.clickable { addIngredient() }
    )
}

@Composable
fun DeleteIngredientButton(onDeleteClick: () -> Unit) {
    Icon(
        painterResource(R.drawable.baseline_close_24),
        contentDescription = "delete",
        modifier = Modifier.clickable { onDeleteClick() }
    )
}

@Preview
@Composable
fun UploadMealPreview() {
    AppTheme(dynamicColor = false) {
        UploadMealScreen(Uri.EMPTY)

    }
}

@Preview
@Composable
fun RatingTrackPreview() {
    AppTheme(dynamicColor = false) {
        RatingTrack(3)
    }
}