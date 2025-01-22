package com.interview.myapplication

import com.interview.myapplication.network.ResponseStatus
import com.interview.myapplication.network.repo.RecentPostRepo
import com.interview.myapplication.usercase.RecentPostUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecentUseCase {

    private lateinit var recentPostUseCase: RecentPostUseCase
    private lateinit var recentRepoImpl: RecentPostRepo
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        recentRepoImpl = mockk()
        recentPostUseCase = RecentPostUseCase(recentRepoImpl)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `use case returns Pending state when search term is empty`() = runTest {
        // Given
        coEvery { recentRepoImpl.getRecentPosts("") } returns flowOf(ResponseStatus.Pending)

        // When
        val result = recentPostUseCase.invoke("")

        // Then
        result.collect { state ->
            assertEquals(ResponseStatus.Pending, state)
        }
    }

    @Test
    fun `use case returns Failure state when repository returns error`() = runTest {
        // Given
        val errorMessage = "Failed to fetch photos"
        coEvery { recentRepoImpl.getRecentPosts("error") } returns flowOf(
            ResponseStatus.Fetching,
            ResponseStatus.ErrorOccurred(errorMessage)
        )

        // When
        val resultFlow = recentPostUseCase.invoke("error")

        // Then
        resultFlow.collect { state ->
            // Assert after flow emits all states
            if (state is ResponseStatus.ErrorOccurred) {
                assertEquals(ResponseStatus.ErrorOccurred(errorMessage), state)
            }
        }
    }
}
