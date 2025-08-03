package uz.alien.nested

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import uz.alien.nested.presentation.MainViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel()
        print("\n\n\n\n")
    }

    @After
    fun tearDown() {
        print("\n\n\n\n")
        Dispatchers.resetMain()
    }

    @Test
    fun `setCurrentCollection should set correct item as current and others as not current`() = runTest {
        val collectionSize = viewModel.collectionsFlow.value.size

        // Test for all valid indices
        for (i in 0 until collectionSize) {
            viewModel.setCurrentCollection(i)

            val state = viewModel.collectionsFlow.value

            // Only one should be current
            assertEquals(1, state.count { it.isCurrent })

            // That one should have the correct ID
            assertEquals(i, state.single { it.isCurrent }.id)

            // All others should be not current
            assertTrue(state.filterNot { it.id == i }.all { !it.isCurrent })
        }
    }

    @Test
    fun `setCurrentCollection should not change anything for invalid id`() = runTest {
        // Given: set a known current item first
        viewModel.setCurrentCollection(0)
        val before = viewModel.collectionsFlow.value

        // When: invalid id (negative)
        viewModel.setCurrentCollection(-1)
        assertEquals(before, viewModel.collectionsFlow.value)

        // When: invalid id (too large)
        viewModel.setCurrentCollection(999)
        assertEquals(before, viewModel.collectionsFlow.value)
    }

    @Test
    fun `debugging in console`() = runTest {
        println("Hello world!")

        println(viewModel.collectionsFlow.value)
    }
}