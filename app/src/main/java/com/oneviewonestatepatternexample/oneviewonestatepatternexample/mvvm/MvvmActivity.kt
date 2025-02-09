package com.oneviewonestatepatternexample.oneviewonestatepatternexample.mvvm


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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MvvmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MvvmViewModel = hiltViewModel()
            val randomNumberState by viewModel.randomNumberState.collectAsState()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Button(onClick = { viewModel.generateRandomNumber() }) {
                    Text("Generate Random Number")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.showToast() }) {
                    Text("Show Toast")
                }
                Spacer(modifier = Modifier.height(16.dp))
                when (randomNumberState) {
                    is MvvmViewModel.RandomNumberState.Idle -> Text("Idle State")
                    is MvvmViewModel.RandomNumberState.Loading -> CircularProgressIndicator()
                    is MvvmViewModel.RandomNumberState.Success -> Text("Random Number: ${(randomNumberState as MvvmViewModel.RandomNumberState.Success).number}")
                }
            }

            // Collect and handle toast messages
            LaunchedEffect(Unit) {
                viewModel.toastMessage.collectLatest { message ->
                    Toast.makeText(this@MvvmActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}