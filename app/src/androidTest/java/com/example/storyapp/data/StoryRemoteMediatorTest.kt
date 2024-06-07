package com.example.storyapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.storyapp.data.network.ApiService
import com.example.storyapp.data.network.DetailStoryResponse
import com.example.storyapp.data.network.ListStoryItem
import com.example.storyapp.data.network.LoginResponse
import com.example.storyapp.data.network.RegisterResponse
import com.example.storyapp.data.network.StoryResponse
import com.example.storyapp.data.network.UploadStoryResponse
import com.example.storyapp.database.StoryDatabase
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoryRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoryDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoryDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoryRemoteMediator(
            mockDb,
            mockApi,
        )
        val pagingState = PagingState<Int, ListStoryItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}
class FakeApiService : ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getStories(page: Int, size: Int): StoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "name $i",
                "description $i",
                "http://example.com/photo$i.jpg",
                "2024-06-05T00:00:00Z",
                i.toDouble(),
                i.toDouble()
            )
            items.add(story)
        }
        val pagedItems = items.subList((page - 1) * size, (page - 1) * size + size)
        return StoryResponse(listStory = pagedItems)
    }

    override suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailStory(id: String): DetailStoryResponse {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        long: RequestBody?
    ): UploadStoryResponse {
        TODO("Not yet implemented")
    }
}