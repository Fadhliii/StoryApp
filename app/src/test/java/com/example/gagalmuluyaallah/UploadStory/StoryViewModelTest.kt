package com.example.gagalmuluyaallah.UploadStory

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.example.gagalmuluyaallah.DataDummy
import com.example.gagalmuluyaallah.MainDispatcherRule
import com.example.gagalmuluyaallah.ResultSealed
import com.example.gagalmuluyaallah.StoryPagingSource
import com.example.gagalmuluyaallah.connection.GeneralRepository
import com.example.gagalmuluyaallah.connection.UserPreference
import com.example.gagalmuluyaallah.getOrAwaitValue
import com.example.gagalmuluyaallah.noopListUpdateCallback
import com.example.gagalmuluyaallah.response.StoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: GeneralRepository

    // Hapus anotasi @Mock untuk StoryViewModel
    private lateinit var StoryViewModelTesting: StoryViewModel

    private lateinit var logMock: MockedStatic<Log>

    @Before
    fun setUp() {
        logMock = Mockito.mockStatic(Log::class.java)
        logMock.`when`<Boolean> { Log.isLoggable(Mockito.anyString(), Mockito.anyInt()) }.thenReturn(true)

        // Inisialisasi StoryViewModel dengan objek asli, melewatkan repository yang dimocking
        StoryViewModelTesting = StoryViewModel(repository)
    }

    @After
    fun tearDown() {
        logMock.close()
    }

    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyQuote = DataDummy.generateDummyStoryResponse()
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummyQuote)

        val expectedStory = MutableLiveData<ResultSealed<PagingData<StoryItem>>>()
        expectedStory.value = ResultSealed.Success(data)

        Mockito.`when`(repository.getAllStories(StoryViewModelTesting.viewModelScope)).thenReturn(expectedStory)

        val actualStory = StoryViewModelTesting.stories.getOrAwaitValue()
        val response = (actualStory as ResultSealed.Success).data

        val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
        )
        differ.submitData(response)

        assertNotNull(differ.snapshot())
        assertEquals(dummyQuote.size, differ.snapshot().size)
        assertEquals(dummyQuote[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<StoryItem> = PagingData.from(emptyList())

        val expectedStory = MutableLiveData<ResultSealed<PagingData<StoryItem>>>()
        expectedStory.value = ResultSealed.Success(data)

        Mockito.`when`(repository.getAllStories(StoryViewModelTesting.viewModelScope)).thenReturn(expectedStory)

        val actualStory = StoryViewModelTesting.stories.getOrAwaitValue()
        val response = (actualStory as ResultSealed.Success).data

        val differ = AsyncPagingDataDiffer(
                diffCallback = StoryAdapter.DIFF_CALLBACK,
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
        )
        differ.submitData(response)

        assertEquals(0, differ.snapshot().size)
    }
}
