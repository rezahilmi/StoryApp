package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.network.ListStoryItem
import com.example.storyapp.data.network.StoryResponse
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _storyList = MutableLiveData<List<ListStoryItem>>()
    var storyList: LiveData<PagingData<ListStoryItem>> =
        repository.getStories().cachedIn(viewModelScope)

    private val _storyResult = MutableLiveData<StoryResponse>()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    //    fun fetchStories() {
    //        viewModelScope.launch {
    //            try {
    //                _isLoading.value = true
    //                _storyList.postValue(repository.getStories())
    //            } catch (e: HttpException) {
    //                val response = repository.getStories()
    //                _storyResult.value = response
    //                val errorBody = e.response()?.errorBody()?.string()
    //                val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
    //                _storyResult.value = errorResponse
    //            } finally {
    //                _isLoading.value = false
    //            }
    //        }
    //    }

}