package uz.alien.nested.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import uz.alien.nested.model.PartUIState
import uz.alien.nested.model.UnitUIState
import kotlin.random.Random.Default.nextBoolean

class MainViewModel : ViewModel() {

    private val _parts = MutableStateFlow<List<PartUIState>>(emptyList())
    val parts: StateFlow<List<PartUIState>> = _parts

    private val _selectedPartIndex = MutableStateFlow(5)
    val selectedPartIndex: StateFlow<Int> = _selectedPartIndex

    init {
        val dummyParts = List(6) { partIndex ->
            PartUIState(
                index = partIndex,
                title = "Part ${partIndex + 1}",
                units = List(30) { unitIndex ->
                    UnitUIState(
                        id = unitIndex,
                        name = "unit ${unitIndex + 1}",
                        progress = (0..100).random(),
                        part = partIndex
                    )
                }
            )
        }
        _parts.value = dummyParts
    }

    fun setSelectedPart(index: Int) {
        _selectedPartIndex.value = index
    }

    fun toggleUnitSelection(unitId: Int) {
        updateCurrentPartUnits { unit ->
            if (unit.id == unitId) unit.copy(isSelected = !unit.isSelected) else unit
        }
    }

    fun selectUnit(unitId: Int) {
        updateCurrentPartUnits { unit ->
            if (unit.id == unitId) unit.copy(isSelected = true) else unit
        }
    }

    fun unselectUnit(unitId: Int) {
        updateCurrentPartUnits { unit ->
            if (unit.id == unitId) unit.copy(isSelected = false) else unit
        }
    }

    fun selectAll() {
        for (i in 0..parts.value[0].units.size) {
            selectUnit(i)
        }
    }

    fun clearAll() {
        for (i in 0..parts.value[0].units.size) {
            unselectUnit(i)
        }
    }

    fun invertAll() {
        for (i in 0..parts.value[0].units.size) {
            toggleUnitSelection(i)
        }
    }

    fun randomSelect() {
        for (i in 0..parts.value[0].units.size) {
            if (nextBoolean()) {
                selectUnit(i)
            } else {
                unselectUnit(i)
            }
        }
    }

    fun isUnitSelected(unitId: Int): Boolean {
        return _parts.value
            .firstOrNull { it.index == _selectedPartIndex.value }
            ?.units
            ?.find { it.id == unitId }
            ?.isSelected ?: false
    }

    fun getSelectedUnits(): List<SelectedUnit> {
        return _parts.value.flatMap { part ->
            part.units
                .filter { it.isSelected }
                .map { unit -> SelectedUnit(part.index, unit.id) }
        }
    }

    private fun updateCurrentPartUnits(transform: (UnitUIState) -> UnitUIState) {
        _parts.update { parts ->
            parts.map { part ->
                if (part.index == _selectedPartIndex.value) {
                    part.copy(units = part.units.map(transform))
                } else part
            }
        }
    }

    data class SelectedUnit(val partIndex: Int, val unitId: Int)
}
