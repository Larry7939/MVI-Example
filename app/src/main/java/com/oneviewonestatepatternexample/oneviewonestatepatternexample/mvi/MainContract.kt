package com.oneviewonestatepatternexample.oneviewonestatepatternexample.mvi

class MainContract {

    // Events that the user can perform
    sealed class Event : UiEvent {
        data object OnRandomNumberClicked : Event()
        data object OnShowToastClicked : Event()
    }

    // UI View States
    data class State(
        val randomNumberState: RandomNumberState
    ) : UiState

    // View State related to Random Number
    sealed class RandomNumberState {
        data object Idle : RandomNumberState()
        data object Loading : RandomNumberState()
        data class Success(val number: Int?) : RandomNumberState()
    }

    // Side effects
    sealed class Effect : UiEffect {
        data class ShowToast(val message: String) : Effect()
    }
}
