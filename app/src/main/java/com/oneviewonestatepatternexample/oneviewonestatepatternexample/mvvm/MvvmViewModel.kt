package com.oneviewonestatepatternexample.oneviewonestatepatternexample.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MvvmViewModel @Inject constructor() : ViewModel() {

    // 각각의 UI 상태를 개별적으로 관리
    private val _randomNumberState = MutableStateFlow<RandomNumberState>(RandomNumberState.Idle)
    val randomNumberState: StateFlow<RandomNumberState> = _randomNumberState.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    fun generateRandomNumber() {
        viewModelScope.launch {
            _randomNumberState.emit(RandomNumberState.Loading)
            delay(1000) // 네트워크 지연 시간
            val random = (0..10).random()
            _randomNumberState.emit(RandomNumberState.Success(random))
            _toastMessage.emit("생성된 값 -> $random")
        }
    }

    fun showToast() {
        viewModelScope.launch {
            val message = when (val state = _randomNumberState.value) {
                is RandomNumberState.Idle -> "아직 값이 생성되지 않았습니다."
                is RandomNumberState.Loading -> "랜덤 값 생성 중입니다."
                is RandomNumberState.Success -> "생성된 값: ${state.number}"
            }
            _toastMessage.emit(message)
        }
    }

    sealed class RandomNumberState {
        data object Idle : RandomNumberState()
        data object Loading : RandomNumberState()
        data class Success(val number: Int) : RandomNumberState()
    }
}