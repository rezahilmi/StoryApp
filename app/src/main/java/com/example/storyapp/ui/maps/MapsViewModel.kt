package com.example.storyapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.network.ListStoryItem
import com.example.storyapp.data.network.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MapsViewModel(private val repository: UserRepository) : ViewModel() {

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    val storyList: LiveData<List<ListStoryItem>> = _storyList

    private val _storyResult = MutableLiveData<StoryResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchStoriesWithLocation() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getStoriesWithLocation()
                _storyList.value = response.listStory
            } catch (e: HttpException) {
                val response = repository.getStoriesWithLocation()
                _storyResult.value = response
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
                _storyResult.value = errorResponse
            } finally {
                _isLoading.value = false
            }
        }
    }

}