package com.example.storyapp.ui.detailStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.DetailStoryResponse
import com.example.storyapp.data.api.ListStoryItem
import com.example.storyapp.data.api.Story
import com.example.storyapp.data.api.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailStoryModel(private val userRepository: UserRepository) : ViewModel() {

    private val _storyDetail = MutableLiveData<DetailStoryResponse>()
    val storyDetail: LiveData<DetailStoryResponse> = _storyDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchDetailStory(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = userRepository.getStoryDetail(id)
                _storyDetail.value = response
            } catch (e: HttpException) {
                val response = userRepository.getStoryDetail(id)
                _storyDetail.value = response
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, DetailStoryResponse::class.java)
                _storyDetail.value = errorResponse
            } finally {
                _isLoading.value = false
            }
        }
    }
}