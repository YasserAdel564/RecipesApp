package com.recipe.app.utils

sealed class MyUiStates {
    object Loading : MyUiStates()
    object Success : MyUiStates()
    object LastPage : MyUiStates()
    data class Error(val message: String) : MyUiStates()
    object NoConnection : MyUiStates()
    object Empty : MyUiStates()
}