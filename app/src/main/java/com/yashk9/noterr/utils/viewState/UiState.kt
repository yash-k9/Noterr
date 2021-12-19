package com.yashk9.noterr.utils.viewState

import com.yashk9.noterr.model.Note

sealed class UiState{
    data class Success(val data: List<Note>): UiState()
    data class Error(val message: String): UiState()
    object Empty: UiState()
    object Loading : UiState()
}