package com.cevdetkilickeser.ktorimplementation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.viewModelFactory
import coil3.compose.AsyncImage
import com.cevdetkilickeser.ktorimplementation.ui.theme.KtorImplementationTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel> {
        viewModelFactory {
            addInitializer(MainViewModel::class) {
                MainViewModel(apiClient = ApiClient())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KtorImplementationTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    App()
                }
            }
        }
    }

    @Composable
    fun App() {
        val productList by mainViewModel.productList.collectAsStateWithLifecycle()

        when (val uiState = productList) {
            is MainUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is MainUiState.Error -> {
                Text(text = uiState.message)
            }

            is MainUiState.Success -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.systemBarsPadding()
                ) {
                    items(uiState.data) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            AsyncImage(
                                model = it.image,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(5.dp)
                            )
                            Text(text = it.title ?: "")
                        }
                    }
                }
            }
        }
    }
}

