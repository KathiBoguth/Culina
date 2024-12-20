package com.example.culina.social

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.culina.R
import com.example.culina.common.BackgroundPanel
import kotlin.random.Random

@Composable
fun Social(
    viewModel: SocialViewModel = hiltViewModel(),
    innerPadding: PaddingValues
) {
    val allMeals by viewModel.allPosts.collectAsState(listOf())
    val allImages by viewModel.allImages.collectAsState(mapOf())

    BackgroundPanel(innerPadding) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Your Feed", style = MaterialTheme.typography.titleLarge)
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allMeals) { meal ->
                    val image = if (meal.image != null) {
                        allImages.getOrElse(meal.image) {
                            viewModel.fetchImage(meal.image)
                            ImageBitmap.imageResource(R.drawable.meal).asAndroidBitmap()
                        }
                    } else {
                        ImageBitmap.imageResource(R.drawable.meal).asAndroidBitmap()

                    }
                    val userName = if (meal.userName.isNullOrBlank()) {
                        "Anonymous"
                    } else {
                        meal.userName!!
                    }
                    CookingCard(
                        userName = userName,
                        imageBitmap = image.asImageBitmap(),
                        title = meal.name,
                        ingredients = meal.ingredients
                    )
                }
            }
        }
    }
}

@Composable
fun CookingCard(
    userName: String,
    imageBitmap: ImageBitmap,
    title: String,
    ingredients: Set<String>,
) {
    val color = getRandomUserIconColor(MaterialTheme.colorScheme)
    Card(
        modifier = Modifier
            .size(width = 280.dp, height = 480.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Image(
                bitmap = imageBitmap,
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Meal Photo",
                contentScale = ContentScale.FillHeight
            )
            Box(
                modifier = Modifier
                    .offset(8.dp, 8.dp)
                    .clip(CircleShape)
                    .background(color.key)
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    userName.first().uppercase(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = color.value
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.3f to MaterialTheme.colorScheme.surface
                        )
                    )
                    .padding(top = 32.dp, start = 12.dp, bottom = 12.dp)
            ) {
                Column {
                    Text(title, style = MaterialTheme.typography.bodyMedium)
                    Row {

                        for (ingredient in ingredients) {
                            InputChip(
                                modifier = Modifier.padding(4.dp),
                                selected = false,
                                label = {
                                    Text(
                                        ingredient,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                },
                                onClick = {})
                        }
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun CookingCardPreview() {
    val image = ImageBitmap.imageResource(R.drawable.meal)
    CookingCard(
        "curry",
        image.asAndroidBitmap().asImageBitmap(),
        "Pepper Plate",
        setOf("pepper", "spices"),
    )
}

fun getRandomUserIconColor(colorScheme: ColorScheme): Map.Entry<Color, Color> {
    val allColorCombos = mapOf(
        colorScheme.primaryContainer to colorScheme.onPrimaryContainer,
        colorScheme.secondaryContainer to colorScheme.onSecondaryContainer,
        colorScheme.surface to colorScheme.onSurface,
        colorScheme.surfaceVariant to colorScheme.onSurfaceVariant,
        colorScheme.surfaceContainer to colorScheme.onSurface,
        colorScheme.primary to colorScheme.onPrimary,
        colorScheme.secondary to colorScheme.onSecondary,
        colorScheme.background to colorScheme.onBackground,
        colorScheme.tertiary to colorScheme.onTertiary,
        colorScheme.tertiaryContainer to colorScheme.onTertiaryContainer,
    )
    val random = Random(System.currentTimeMillis())
    return allColorCombos.entries.elementAt(random.nextInt(allColorCombos.size))

}