package com.yashk9.noterr.utils.viewState

import com.yashk9.noterr.model.Note

sealed class DetailState{
    data class Success(val value: Note): DetailState()
    data class Error(val error: String): DetailState()
    object Loading : DetailState()
}
