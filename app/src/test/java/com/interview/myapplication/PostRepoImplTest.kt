package com.interview.myapplication

import android.util.Log
import app.cash.turbine.test
import com.interview.myapplication.data.Items
import com.interview.myapplication.data.Media
import com.interview.myapplication.data.PostResponse
import com.interview.myapplication.network.ResponseStatus
import com.interview.myapplication.network.api.RecentPostService
import com.interview.myapplication.network.repo.RecentRepoImpl
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import io.mockk.mockk
import io.mockk.unmockkAll
import java.net.UnknownHostException
import io.mockk.coEvery
import kotlinx.coroutines.test.*
import okhttp3.ResponseBody.Companion.toResponseBody


@OptIn(ExperimentalCoroutinesApi::class)
class PostRepoImplTest {

    private lateinit var recentPostService: RecentPostService
    private lateinit var repository: RecentRepoImpl
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        every { Log.e(any(), any()) } returns 0
        every { Log.e(any(), any(), any()) } returns 0

        recentPostService = mockk()
        repository = RecentRepoImpl(recentPostService)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `empty query returns Pending state`() = runTest {
        repository.getRecentPosts("").test {
            assertEquals(ResponseStatus.Pending, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `null API response body returns Failure state`() = runTest {
        // Given
        coEvery { recentPostService.getRecentPosts(tag = "nature") } returns Response.success(null)

        // When
        repository.getRecentPosts("nature").test {
            assertEquals(ResponseStatus.Fetching, awaitItem())
            val failureState = awaitItem() as ResponseStatus.ErrorOccurred
            assertEquals("An error occurred. Please try again later.", failureState.message)
            awaitComplete()
        }
    }

    @Test
    fun `no internet connection returns Failure state`() = runTest {
        // Given
        coEvery { recentPostService.getRecentPosts(tag = "offline") } throws UnknownHostException()

        // When
        repository.getRecentPosts("offline").test {
            assertEquals(ResponseStatus.Fetching, awaitItem())
            val failureState = awaitItem() as ResponseStatus.ErrorOccurred
            assertEquals("Unable to connect. Please check your internet connection and try again.", failureState.message)
            awaitComplete()
        }
    }

    @Test
    fun `successful API call returns Success state`() = runTest {
        // Given
        val mockItem = Items(
            author = "Julie Hall",
            authorId = "1345",
            dateTaken = "2015-11-04",
            description = "Beautiful sunset",
            link = "https://images.example.com/photo.jpg",
            media = Media("https://images.example.com/media.jpg"),
            published = "2015-06-19",
            tags = "ocean",
            title = "Sunset"
        )
        val response = PostResponse(
            title = "Public Photos",
            link = "https://images.example.com/photos",
            items = listOf(mockItem),
            description = "",
            modified = "",
            generator = ""
        )

        coEvery { recentPostService.getRecentPosts(tag = "sunset") } returns Response.success(response)

        // When
        repository.getRecentPosts("sunset").test {
            assertEquals(ResponseStatus.Fetching, awaitItem())
            val successState = awaitItem() as ResponseStatus.FetchedSuccessfully
            assertEquals(1, successState.thumbnail.size)
            assertEquals("Sunset by the Ocean", successState.thumbnail.first().title)
            awaitComplete()
        }
    }

    @Test
    fun `api error returns Failure state`() = runTest {
        // Given
        val responseBody = "API error".toResponseBody()
        coEvery { recentPostService.getRecentPosts(tag = "error") } returns Response.error(500, responseBody)

        // When
        repository.getRecentPosts("error").test {
            assertEquals(ResponseStatus.Fetching, awaitItem())
            val failureState = awaitItem() as ResponseStatus.ErrorOccurred
            assertEquals("An error occurred. Please try again later.", failureState.message)
            awaitComplete()
        }
    }
}
