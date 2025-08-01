package uz.alien.nested.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import uz.alien.nested.model.CollectionUIState
import uz.alien.nested.model.PartUIState
import uz.alien.nested.model.SelectedUnit
import uz.alien.nested.model.UnitUIState
import kotlin.random.Random.Default.nextBoolean

class MainViewModel : ViewModel() {
    private val _collectionsFlow = MutableStateFlow(
        listOf(
            CollectionUIState(id = 0, title = "Beginner", partCount = 4, unitCount = 20),
            CollectionUIState(id = 1, title = "Essential", partCount = 6, unitCount = 30)
        )
    )
    val collectionsFlow: StateFlow<List<CollectionUIState>> = _collectionsFlow.asStateFlow()

    private val partsFlows: MutableList<MutableStateFlow<List<PartUIState>>> = mutableListOf()
    private val unitFlows: MutableList<List<MutableStateFlow<List<UnitUIState>>>> = mutableListOf()

    init {
        var partId = 0
        var unitId = 0
        _collectionsFlow.value.forEachIndexed { collectionIndex, collection ->
            val parts = List(collection.partCount) {
                PartUIState(
                    id = partId++,
                    title = "${it + 1}",
                    collectionId = collection.id,
                    unitCount = collection.unitCount
                )
            }
            partsFlows.add(MutableStateFlow(parts))
            unitFlows.add(
                List(collection.partCount) { partIndex ->
                    MutableStateFlow(
                        List(collection.unitCount) {
                            UnitUIState(
                                id = unitId++,
                                name = "Unit ${it + 1}",
                                progress = (0..100).random(),
                                collectionId = collection.id,
                                partId = parts[partIndex].id
                            )
                        }
                    )
                }
            )
        }
    }

    fun getPartsFlow(collectionId: Int): StateFlow<List<PartUIState>> {
        return partsFlows.getOrNull(collectionId) ?: MutableStateFlow(emptyList())
    }

    fun getUnitsFlow(collectionId: Int, partId: Int): StateFlow<List<UnitUIState>> {
        return unitFlows.getOrNull(collectionId)?.getOrNull(partId) ?: MutableStateFlow(emptyList())
    }

    fun setCurrentCollection(collectionId: Int) {
        _collectionsFlow.update { collections ->
            collections.map { collection ->
                if (collection.id == collectionId) collection.copy(isCurrent = true)
                else collection.copy(isCurrent = false)
            }
        }
        setCurrentPart(collectionId, 0)
    }

    fun setCurrentPart(collectionId: Int, partId: Int) {
        partsFlows.getOrNull(collectionId)?.update { parts ->
            parts.map { part ->
                if (part.id == partId) part.copy(isCurrent = true)
                else part.copy(isCurrent = false)
            }
        }
    }

    fun toggleUnitSelection(collectionId: Int, partId: Int, unitId: Int) {
        unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.update { units ->
            val updatedUnits = units.map { unit ->
                if (unit.id == unitId) unit.copy(isSelected = !unit.isSelected) else unit
            }
            updateSelectedUnitCount(collectionId, partId, updatedUnits)
            updatedUnits
        }
    }

    fun selectUnit(collectionId: Int, partId: Int, unitId: Int) {
        unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.update { units ->
            val updatedUnits = units.map { unit ->
                if (unit.id == unitId) unit.copy(isSelected = true) else unit
            }
            updateSelectedUnitCount(collectionId, partId, updatedUnits)
            updatedUnits
        }
    }

    fun unselectUnit(collectionId: Int, partId: Int, unitId: Int) {
        unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.update { units ->
            val updatedUnits = units.map { unit ->
                if (unit.id == unitId) unit.copy(isSelected = false) else unit
            }
            updateSelectedUnitCount(collectionId, partId, updatedUnits)
            updatedUnits
        }
    }

    fun selectAll(collectionId: Int, partId: Int) {
        unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.update { units ->
            val updatedUnits = units.map { it.copy(isSelected = true) }
            updateSelectedUnitCount(collectionId, partId, updatedUnits)
            updatedUnits
        }
    }

    fun clearAll(collectionId: Int, partId: Int) {
        unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.update { units ->
            val updatedUnits = units.map { it.copy(isSelected = false) }
            updateSelectedUnitCount(collectionId, partId, updatedUnits)
            updatedUnits
        }
    }

    fun invertAll(collectionId: Int, partId: Int) {
        unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.update { units ->
            val updatedUnits = units.map { it.copy(isSelected = !it.isSelected) }
            updateSelectedUnitCount(collectionId, partId, updatedUnits)
            updatedUnits
        }
    }

    fun randomSelect(collectionId: Int, partId: Int) {
        unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.update { units ->
            val updatedUnits = units.map { it.copy(isSelected = nextBoolean()) }
            updateSelectedUnitCount(collectionId, partId, updatedUnits)
            updatedUnits
        }
    }

    fun selectAllInCollection(collectionId: Int) {
        unitFlows.getOrNull(collectionId)?.forEachIndexed { partId, unitsFlow ->
            unitsFlow.update { units ->
                val updatedUnits = units.map { it.copy(isSelected = true) }
                updateSelectedUnitCount(collectionId, partId, updatedUnits)
                updatedUnits
            }
        }
    }

    fun clearAllInCollection(collectionId: Int) {
        unitFlows.getOrNull(collectionId)?.forEachIndexed { partId, unitsFlow ->
            unitsFlow.update { units ->
                val updatedUnits = units.map { it.copy(isSelected = false) }
                updateSelectedUnitCount(collectionId, partId, updatedUnits)
                updatedUnits
            }
        }
    }

    fun isUnitSelected(collectionId: Int, partId: Int, unitId: Int): Boolean {
        return unitFlows.getOrNull(collectionId)?.getOrNull(partId)?.value?.find { it.id == unitId }?.isSelected ?: false
    }

    fun getSelectedUnits(): List<SelectedUnit> {
        return unitFlows.flatMapIndexed { collectionId, parts ->
            parts.flatMapIndexed { partId, unitsFlow ->
                unitsFlow.value.filter { it.isSelected }.map { unit ->
                    SelectedUnit(
                        collectionId = collectionId,
                        partId = partId,
                        unitId = unit.id
                    )
                }
            }
        }
    }

    private fun updateSelectedUnitCount(collectionId: Int, partId: Int, units: List<UnitUIState>) {
        val selectedCount = units.count { it.isSelected }
        partsFlows.getOrNull(collectionId)?.update { parts ->
            parts.map { part ->
                if (part.id == partId) {
                    part.copy(
                        selectedUnitCount = selectedCount,
                        isSelected = selectedCount > 0
                    )
                } else part
            }
        }
        _collectionsFlow.update { collections ->
            collections.map { collection ->
                if (collection.id == collectionId) {
                    val totalSelected = partsFlows.getOrNull(collectionId)?.value?.sumOf { it.selectedUnitCount } ?: 0
                    collection.copy(
                        selectedUnitCount = totalSelected,
                        isSelected = totalSelected > 0
                    )
                } else collection
            }
        }
    }

    fun addCollection(collection: CollectionUIState) {
        _collectionsFlow.update { it + collection }
        var partId = partsFlows.sumOf { it.value.size }
        partsFlows.add(
            MutableStateFlow(
                List(collection.partCount) {
                    PartUIState(
                        id = partId++,
                        title = "${it + 1}",
                        collectionId = collection.id,
                        unitCount = collection.unitCount
                    )
                }
            )
        )
        var unitId = unitFlows.sumOf { parts -> parts.sumOf { it.value.size } }
        unitFlows.add(
            List(collection.partCount) { partIndex ->
                MutableStateFlow(
                    List(collection.unitCount) {
                        UnitUIState(
                            id = unitId++,
                            name = "Unit ${it + 1}",
                            progress = (0..100).random(),
                            collectionId = collection.id,
                            partId = partIndex
                        )
                    }
                )
            }
        )
    }
}