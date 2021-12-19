package com.yashk9.noterr.view.viewModel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yashk9.noterr.data.datastore.AppDatastore
import com.yashk9.noterr.model.Note
import com.yashk9.noterr.repo.NoteRepo
import com.yashk9.noterr.utils.Result
import com.yashk9.noterr.utils.viewState.DetailState
import com.yashk9.noterr.utils.viewState.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: NoteRepo,
    private val uiDatastore: AppDatastore
): ViewModel() {

    private val TAG = "NoteViewModel"
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)


    val queryText = MutableStateFlow("")
    val uiState = _uiState.asStateFlow()
    val uiMode = uiDatastore.uiMode

    fun addNote(note : Note){
        viewModelScope.launch {
            repo.addNote(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            repo.deleteNoteById(note.id)
        }
    }

    @ExperimentalCoroutinesApi
    fun getQueryNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            queryText.flatMapLatest {
                Log.d(TAG, "getAllNotes: ${Thread.currentThread().name}")
                repo.searchNote(it)
            }.collect { result ->
                if(!result.isNullOrEmpty()){
                    _uiState.value = UiState.Success(result)
                }else{
                    _uiState.value = UiState.Empty
                }
            }
        }
    }

    fun getAllNotes(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repo.getAllNotes().collect { result ->
                if(!result.isNullOrEmpty()){
                    _uiState.value = UiState.Success(result)
                }else{
                    _uiState.value = UiState.Empty
                }
            }
        }
    }

    fun setUiMode(isEnabled: Boolean){
        viewModelScope.launch {
            uiDatastore.saveUiToDatastore(isEnabled)
        }
    }

}