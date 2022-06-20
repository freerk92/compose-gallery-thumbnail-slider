package com.example.testapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import com.example.testapplication.ui.theme.TestApplicationTheme
import com.google.accompanist.pager.ExperimentalPagerApi


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestApplicationTheme {
                // A surface container using the 'background' color from the theme
                SimpleComposable()
            }
        }
    }
}

@Composable
fun SimpleComposable()
{
    Scaffold(topBar = { TopAppBar(title = { Text("TopAppBar") }, backgroundColor = MaterialTheme.colors.background) }, content = { padding ->

            imageViewerUI(padding)

    })
}

@Composable
fun SwipeBar()
{

}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterialApi::class)
@Composable
fun imageViewerUI(padding : PaddingValues) {
    var selectedItem by remember { mutableStateOf(1) }
    var selectedUrl by remember { mutableStateOf("")}

        Column(Modifier.fillMaxSize().padding(bottom = 100.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(
                            if (selectedUrl.isNullOrEmpty()) randomSampleImageUrl(
                                seed = selectedItem,
                                width = 600,
                                height = 1200
                            ) else selectedUrl
                        ).build(),
                ),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxSize()
            )
        }
        var list = (1..10).toList()
        LazyRow(Modifier.fillMaxSize(), verticalAlignment = Alignment.Bottom){
            items(list) {
                val isSelected = it == selectedItem

                Card(
                    backgroundColor = Color.LightGray,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp),
                    border = if(isSelected) BorderStroke(2.dp, Color.Red) else null,
                    onClick = { selectedItem = it
                        selectedUrl = randomSampleImageUrl(it, 600, 1200)
                    }
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = rememberRandomSampleImageUrl(
                                seed = it,
                                150, 150
                            ),
                        ),
                        contentScale = ContentScale.Fit,
                        contentDescription = null
                    )
                }
            }
        }

}



/**
 * Simple pager item which displays an image
 */
@OptIn(ExperimentalCoilApi::class)
@Composable
internal fun PagerSampleItem(
    page: Int,
    modifier: Modifier = Modifier,
) {
    Box(modifier.fillMaxSize().background(Color.Blue)) {
        // Our page content, displaying a random image
        Image(
            painter = rememberImagePainter(
                data = rememberRandomSampleImageUrl(seed = page+1, width = 150, height = 150),
            ),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
    }
}

private val rangeForRandom = (0..100000)

fun randomSampleImageUrl(
    seed: Int = rangeForRandom.random(),
    width: Int = 300,
    height: Int = width,
): String {
    return "https://picsum.photos/seed/$seed/$width/$height"
}

/**
 * Remember a URL generate by [randomSampleImageUrl].
 */
@Composable
fun rememberRandomSampleImageUrl(
    seed: Int = rangeForRandom.random(),
    width: Int = 300,
    height: Int = width,
): String = remember { randomSampleImageUrl(seed, width, height) }

@Preview
@Composable
fun ComposablePreview() {
    SimpleComposable()
}
