package com.example.storyapp.data

import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.api.DetailStoryResponse
import com.example.storyapp.data.api.LoginResponse
import com.example.storyapp.data.api.RegisterResponse
import com.example.storyapp.data.api.StoryResponse
import com.example.storyapp.data.api.UploadStoryResponse
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
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

    suspend fun getStories(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun getStoryDetail(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun uploadImage(imageFile: MultipartBody.Part, description: RequestBody): UploadStoryResponse {
        return apiService.uploadImage(imageFile, description)
    }

    companion object {
        fun getInstance(apiService: ApiService, userPreference: UserPreference) = UserRepository(apiService, userPreference)
    }
}
