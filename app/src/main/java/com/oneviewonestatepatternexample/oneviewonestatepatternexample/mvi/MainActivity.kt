package com.oneviewonestatepatternexample.oneviewonestatepatternexample.mvi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.setEvent(MainContract.Event.OnRandomNumberClicked) }) {
                    Text("Generate Random Number")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.setEvent(MainContract.Event.OnShowToastClicked) }) {
                    Text("Show Toast")
                }
                Spacer(modifier = Modifier.height(16.dp))
                when (val randomNumberState = state.randomNumberState) {
                    is MainContract.RandomNumberState.Idle -> Text("Idle State")
                    is MainContract.RandomNumberState.Loading -> CircularProgressIndicator()
                    is MainContract.RandomNumberState.Success -> Text("Random Number: ${randomNumberState.number}")
                }
            }

            // Collect and handle effects
            LaunchedEffect(Unit) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is MainContract.Effect.ShowToast -> {
                            Toast.makeText(this@MainActivity, effect.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
