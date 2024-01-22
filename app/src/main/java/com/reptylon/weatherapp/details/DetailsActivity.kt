package com.reptylon.weatherapp.details

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.viewmodel.ext.android.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.reptylon.weatherapp.R
import com.reptylon.weatherapp.ui.theme.WeatherAppTheme


class DetailsActivity : ComponentActivity() {
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id = intent.getStringExtra("id") ?: ""
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black),
                    color = MaterialTheme.colorScheme.background
                ) {


                    DetailsScreen(viewModel, id, modifier = Modifier)
                }
            }
        }
    }
}

@Composable
fun DetailsScreen(viewModel: DetailsViewModel, id: String, modifier: Modifier = Modifier) {
    LaunchedEffect(id) {
        viewModel.loadCharacterDetail(id)
    }
    val characterDetail by viewModel.characterDetail.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = "${characterDetail?.characters?.image}",
            contentDescription = "Image of ${characterDetail?.characters?.name}",
            placeholder = painterResource(id = R.drawable.placeholder),
            modifier = Modifier
                .size(175.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val weatherData = mapOf(
            "Name" to "${characterDetail?.characters?.name}",
            "Species" to "${characterDetail?.characters?.species}",
            "Gender" to "${characterDetail?.characters?.gender}",
            "Origin" to "${characterDetail?.characters?.origin?.name}"
        )

        weatherData.forEach { (key, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = key, fontWeight = FontWeight.Bold)
                Text(text = value)
            }
        }
    }
}
