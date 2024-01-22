package com.reptylon.weatherapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.viewmodel.ext.android.viewModel
// import com.google.android.material.search.SearchBar
import com.reptylon.weatherapp.data.Origin
import com.reptylon.weatherapp.details.DetailsActivity
import com.reptylon.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel : MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getData()

        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {

                            MyTopView(viewModel = viewModel)
                        },

//                    content = {
//                        PeopleList(viewModel, onClick = { id -> navigateToDetails(id) })
//                    },
                        floatingActionButton = {
                            FloatingActionButton(onClick = { }) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                            }
                        }
                    ){innerPadding->
                        Column (
                            modifier = Modifier
                                .padding(innerPadding),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                        ){
                            MainView(viewModel, onClick = { id -> navigateToDetails(id)})
                        }

                    }
                }
            }
        }
    }

    private fun navigateToDetails(id: String) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}



@Composable
fun MainView(viewModel: MainViewModel, onClick: (String) -> Unit) {
    val uiState by viewModel.immutableCharacterData.observeAsState(UiState())
    // val pierwszyCharacter = characters.firstOrNull()
    val query by viewModel.filterQuery.observeAsState("")


    when {
        uiState.isLoading -> {
            // wywołaj funkcję która pokaże loader dla użytkownika
            CircularProgressIndicator(
                modifier = Modifier.width(25.dp)
            )
        }
        uiState.error != null -> {
            // wywołaj funkjce która pokaże komponent typu Snackbar
            Toast.makeText(LocalContext.current, "${uiState.error}", Toast.LENGTH_SHORT).show()
        }
        uiState.characters != null -> {
            // wywołaj funkcję do statków
            uiState.characters?.let { restCharacters ->
                restCharacters.filter { it.name.contains(query, true) }.let { characters ->
                    LazyColumn {
                        items(characters) { character ->
                            Log.d("MainView", "Loaded element: $character")
                            CharacterView(
                                name = character.name,
                                species = character.species,
                                image = character.image,
                                gender = character.gender,
                                origin = character.origin,
                                onClick = { onClick(character.id) }
                            )
                        }
                    }
                }
            }
        }
        else -> {
            Log.e("MainView", "Żaden stan widoku nie został zdefiniowany $uiState")
        }
    }
}



@Composable
fun CharacterView(name: String, species: String, gender: String, origin: Origin, image: String?, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        AsyncImage(
            model = "$image",
            contentDescription = "Image of $name",
            placeholder = painterResource(id = R.drawable.placeholder),
            modifier = Modifier
                .size(175.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        val weatherData = mapOf(
            "Name" to "$name",
            "Species" to "$species",
            "Gender" to "$gender",
            "Origin" to "${origin.name}"
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

//@Composable
//fun WeatherTile(modifier: Modifier = Modifier) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Image(
//            painter = painterResource(id = R.drawable.logo),
//            contentDescription = "Logo",
//            modifier = Modifier
//                .size(100.dp)
//                .clip(MaterialTheme.shapes.medium)
//                .background(MaterialTheme.colorScheme.primary)
//                .padding(16.dp)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        val weatherData = mapOf(
//            "Miasto" to "Kraków",
//            "Temperatura" to "10°K",
//            "Wilgotność" to "60%",
//            "Prawdopodobieństwo opadów" to "50%"
//        )
//
//        weatherData.forEach { (key, value) ->
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(text = key, fontWeight = FontWeight.Bold)
//                Text(text = value)
//            }
//        }
//    }
//}


//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopView(viewModel: MainViewModel) {
    var searchText by remember { mutableStateOf("") }

    SearchBar(
        modifier = Modifier.fillMaxWidth(),
        query = searchText,
        onQueryChange = { wpisywanyTekst -> searchText = wpisywanyTekst },
        onSearch = { viewModel.updateFilterQuery(it) },
        placeholder = { Text(text = "Wyszukaj...") },
        active = false,
        onActiveChange = { },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            Image(
                modifier = Modifier.clickable {
                    searchText = ""
                    viewModel.updateFilterQuery("")
                },
                imageVector = Icons.Default.Clear,
                contentDescription = "Clear"
            )
        }
    ) {

    }
}