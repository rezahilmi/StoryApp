package com.example.storyapp.data

import androidx.lifecycle.liveData
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.api.DetailStoryResponse
import com.example.storyapp.data.api.LoginResponse
import com.example.storyapp.data.api.RegisterResponse
import com.example.storyapp.data.api.Story
import com.example.storyapp.data.api.StoryResponse
import com.example.storyapp.data.api.UploadStoryResponse
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.HttpException
import java.io.File

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

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
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
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference) = UserRepository(apiService, userPreference)
//        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepository {
//            return instance ?: synchronized(this) {
//                instance ?: UserRepository(apiService, userPreference).also { instance = it }
//            }
//        }
    }
}
