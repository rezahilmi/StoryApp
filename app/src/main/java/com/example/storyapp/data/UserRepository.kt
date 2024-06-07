package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.network.ApiService
import com.example.storyapp.data.network.DetailStoryResponse
import com.example.storyapp.data.network.ListStoryItem
import com.example.storyapp.data.network.LoginResponse
import com.example.storyapp.data.network.RegisterResponse
import com.example.storyapp.data.network.StoryResponse
import com.example.storyapp.data.network.UploadStoryResponse
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.database.StoryDatabase
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase
) {

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        val loginResponse = apiService.login(email, password)
        if (loginResponse.error == false) {
            userPreference.saveSession(
                UserModel(
                    loginResponse.loginResult?.userId ?: "",
                    loginResponse.loginResult?.name ?: "",
                    loginResponse.loginResult?.token ?: ""
                )
            )
        }
        return loginResponse
    }


    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(): StoryResponse {
        return apiService.getStoriesWithLocation()
    }

    suspend fun getStoryDetail(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun uploadImage(imageFile: MultipartBody.Part, description: RequestBody,lat: RequestBody?, lon: RequestBody?): UploadStoryResponse {
        return apiService.uploadImage(imageFile, description, lat, lon)
    }

    companion object {
        fun getInstance(apiService: ApiService,
                        userPreference: UserPreference,
                        storyDatabase: StoryDatabase) = UserRepository(apiService, userPreference, storyDatabase)
    }
}
