package com.oneviewonestatepatternexample.oneviewonestatepatternexample.mvi

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    // State 최초 값 초기화
    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            randomNumberState = MainContract.RandomNumberState.Idle
        )
    }

    override fun handleEvent(event: MainContract.Event) {
        when (event) {
            is MainContract.Event.OnRandomNumberClicked -> generateRandomNumber()
            is MainContract.Event.OnShowToastClicked -> {
                when (val state = uiState.value.randomNumberState) {
                    is MainContract.RandomNumberState.Idle -> {
                        setEffect { MainContract.Effect.ShowToast(message = "아직 값이 생성되지 않았습니다.") }
                    }

                    is MainContract.RandomNumberState.Loading -> {
                        setEffect { MainContract.Effect.ShowToast(message = "랜덤 값 생성 중입니다.") }
                    }

                    is MainContract.RandomNumberState.Success -> {
                        setEffect { MainContract.Effect.ShowToast(message = "생성된 값 -> ${state.number}") }
                    }
                }
            }
        }
    }

    private fun generateRandomNumber() {
        viewModelScope.launch {
            setState { copy(randomNumberState = MainContract.RandomNumberState.Loading) }
            delay(1000) // 네트워크 지연 시간
            val random = (0..10).random()
            launch {
                setState { copy(randomNumberState = MainContract.RandomNumberState.Success(random)) }
            }
            launch {
                setEffect { MainContract.Effect.ShowToast("생성된 값 -> $random") }
            }
        }
    }
}
