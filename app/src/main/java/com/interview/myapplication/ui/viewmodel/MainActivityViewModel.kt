package com.interview.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interview.myapplication.data.Posts
import com.interview.myapplication.network.ResponseStatus
import com.interview.myapplication.usercase.RecentPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class MainActivityViewModel @Inject constructor(private val recentPostUseCase: RecentPostUseCase) : ViewModel() {

    private val _imagePostsFetchState = MutableStateFlow<ResponseStatus>(ResponseStatus.Pending)
    val imagePostsFetchState: StateFlow<ResponseStatus> = _imagePostsFetchState.asStateFlow()

    private val _tagQuery = MutableStateFlow("")
    val tagQuery: StateFlow<String> = _tagQuery.asStateFlow()

    private val _currentPost = MutableStateFlow(
        Posts(link = "", title = "No Image Selected", description = "", author = "", dataTaken = "")
    )
    val currentPost: StateFlow<Posts> = _currentPost.asStateFlow()

    init {
        getRecentPosts()
    }

    private fun getRecentPosts() {
        viewModelScope.launch {
            _tagQuery
                .debounce(400)
                .distinctUntilChanged()
                .flatMapLatest { tag->
                    if (tag.isBlank()) {
                        flowOf(ResponseStatus.Pending)
                    } else {
                        recentPostUseCase.invoke(tag)
                    }
                }
                .catch { error ->
                    handleFetchError(error)
                }
                .collect { state ->
                    _imagePostsFetchState.value = state
                }
        }
    }

    // Handle errors during fetch operation
    private fun handleFetchError(error: Throwable) {
        val errorMessage = error.message ?: "Unknown error"
        _imagePostsFetchState.value = ResponseStatus.ErrorOccurred(errorMessage)
    }

    // Sets the search query and triggers an update
    fun setSearchTag(newQuery: String) {
        viewModelScope.launch {
            _tagQuery.emit(newQuery)
        }
    }

    // Updates the selected image
    fun setSelectedPhoto(image: Posts) {
        _currentPost.value = image
    }

    // Reset the state when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        resetState()
        Log.d("ViewModel", "ViewModel reset: State variables reverted to default values.")
    }

    // Reset the state variables
    private fun resetState() {
        _imagePostsFetchState.value = ResponseStatus.Pending
        _tagQuery.value = ""
        _currentPost.value = Posts(link = "", title = "No Image Selected", description = "", author = "", dataTaken = "")
    }

}