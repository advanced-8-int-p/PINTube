package com.example.pintube.ui.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainSharedViewModel: ViewModel() {

    private val _motionState = MutableStateFlow(MotionState.START)
    val motionState: StateFlow<MotionState> = _motionState

    private val _viewState = MutableStateFlow(false)
    val viewState: StateFlow<Boolean> = _viewState.asStateFlow()
    fun updateMotionState(state: MotionState) {
        _motionState.value = state
    }

    fun updateViewState(boolean: Boolean) {
        _viewState.value = boolean
    }
}