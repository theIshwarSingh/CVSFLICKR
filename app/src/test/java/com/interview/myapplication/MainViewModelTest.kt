package com.interview.myapplication

import com.interview.myapplication.data.Posts
import com.interview.myapplication.network.ResponseStatus
import com.interview.myapplication.ui.viewmodel.MainActivityViewModel
import com.interview.myapplication.usercase.RecentPostUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoListViewModelTest {

    private lateinit var mainActivityViewModel: MainActivityViewModel
    private lateinit var recentPostUseCase: RecentPostUseCase
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        recentPostUseCase = mockk()
        mainActivityViewModel = MainActivityViewModel(recentPostUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle`() = runTest {
        // Given
        coEvery { recentPostUseCase.invoke(any()) } returns flowOf(ResponseStatus.Pending)

        // When
        val currentState = mainActivityViewModel.imagePostsFetchState.value

        // Then
        assertEquals(ResponseStatus.Pending, currentState)
    }

    @Test
    fun `selected image updates correctly`() = runTest {
        // Given
        val testImage = Posts(
            link = "https://images.example.com/random29.jpg",
            title = "Chicago Skyline",
            description = "Night view of the city skyline",
            author = "Tom Silver",
            dataTaken = "2021-11-24"
        )

        // When
        mainActivityViewModel.setSelectedPhoto(testImage)

        // Then
        assertEquals(testImage, mainActivityViewModel.currentPost.value)
    }
    @Test
    fun `failure state when fetching photos`() = runTest {
        // Given
        val errorMessage = "Failed to fetch photos"
        coEvery { recentPostUseCase.invoke("error") } returns flowOf(
            ResponseStatus.Fetching,
            ResponseStatus.ErrorOccurred(errorMessage)
        )

        // When
        mainActivityViewModel.setSearchTag("error")

        // Then
        advanceUntilIdle()
        assertEquals(ResponseStatus.ErrorOccurred(errorMessage), mainActivityViewModel.imagePostsFetchState.value)
    }

    @Test
    fun `search query triggers successful state change`() = runTest {
        // Given
        val mockImages = listOf(
            Posts(
                link = "https://example.com/photo1.jpg",
                title = "Sunset View",
                description = "A beautiful sunset photo",
                author = "Jane Doe",
                dataTaken = "2025-01-14"
            )
        )

        coEvery { recentPostUseCase.invoke("") } returns flowOf(ResponseStatus.Pending)
        coEvery { recentPostUseCase.invoke("nature") } returns flowOf(
            ResponseStatus.Fetching,
            ResponseStatus.FetchedSuccessfully(mockImages)
        )

        // When
        mainActivityViewModel.setSearchTag("nature")

        // Then
        advanceUntilIdle()
        assertEquals(ResponseStatus.FetchedSuccessfully(mockImages), mainActivityViewModel.imagePostsFetchState.value)
    }
}
