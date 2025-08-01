package uz.alien.nested

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import uz.alien.nested.ui.MainViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import uz.alien.nested.model.CollectionUIState

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
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Shunchaki test qilib ko'rish uchun yozib ko'ramiz`() = runTest {

        fun add(a: Int, b: Int): Int {
            return a + b
        }

        val a = 5
        val b = 7

        val result = add(a, b)
        val actuallyResult = a + b

        assertEquals(result, actuallyResult)
    }

    @Test
    fun `toggleUnitSelection should toggle isSelected and update selectedUnitCount`() = runTest {
        val collectionId = 0
        val partId = 0
        val unitId = 0

        assertFalse(viewModel.isUnitSelected(collectionId, partId, unitId))
        assertEquals(0, viewModel.getPartsFlow(collectionId).value[partId].selectedUnitCount)
        assertEquals(0, viewModel.collectionsFlow.value[collectionId].selectedUnitCount)

        viewModel.toggleUnitSelection(collectionId, partId, unitId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(viewModel.isUnitSelected(collectionId, partId, unitId))
        assertEquals(1, viewModel.getPartsFlow(collectionId).value[partId].selectedUnitCount)
        assertEquals(1, viewModel.collectionsFlow.value[collectionId].selectedUnitCount)

        viewModel.toggleUnitSelection(collectionId, partId, unitId)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(viewModel.isUnitSelected(collectionId, partId, unitId))
        assertEquals(0, viewModel.getPartsFlow(collectionId).value[partId].selectedUnitCount)
        assertEquals(0, viewModel.collectionsFlow.value[collectionId].selectedUnitCount)
    }

    @Test
    fun `selectAll should select all units in a part and update selectedUnitCount`() = runTest {
        val collectionId = 0
        val partId = 0

        viewModel.selectAll(collectionId, partId)
        testDispatcher.scheduler.advanceUntilIdle()

        val units = viewModel.getUnitsFlow(collectionId, partId).value
        assertTrue(units.all { it.isSelected })
        assertEquals(units.size, viewModel.getPartsFlow(collectionId).value[partId].selectedUnitCount)
        assertEquals(units.size, viewModel.collectionsFlow.value[collectionId].selectedUnitCount)
    }

    @Test
    fun `setCurrentCollection should set isCurrent for selected collection and reset others`() = runTest {
        val collectionId = 1

        viewModel.setCurrentCollection(collectionId)
        testDispatcher.scheduler.advanceUntilIdle()

        val collections = viewModel.collectionsFlow.value
        assertTrue(collections[collectionId].isCurrent)
        assertFalse(collections[0].isCurrent)
    }

    @Test
    fun `addCollection should add new collection and update flows`() = runTest {
        val newCollection =
            CollectionUIState(id = 2, title = "Advanced", partCount = 3, unitCount = 15)
        val initialCollectionCount = viewModel.collectionsFlow.value.size

        viewModel.addCollection(newCollection)
        testDispatcher.scheduler.advanceUntilIdle()

        val collections = viewModel.collectionsFlow.value
        assertEquals(initialCollectionCount + 1, collections.size)
        assertTrue(collections.any { it.id == 2 && it.title == "Advanced" })
        assertEquals(3, viewModel.getPartsFlow(2).value.size)
        assertEquals(15, viewModel.getUnitsFlow(2, 0).value.size)
    }
}