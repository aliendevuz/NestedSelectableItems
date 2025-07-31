package uz.alien.nested.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TestViewModel : ViewModel() {

    private val _isSelecting = MutableStateFlow(false)
    val isSelecting: StateFlow<Boolean> = _isSelecting.asStateFlow()

    fun setSelecting(selecting: Boolean) {
        _isSelecting.value = selecting
    }

    private val _units = MutableStateFlow<List<UnitUIState>>(emptyList())
    val units = _units

    init {
        val dummyUnits = List(50) { unitIndex ->
            UnitUIState(
                id = unitIndex,
                name = "unit ${unitIndex + 1}",
                progress = (0..100).random(),
                part = 0
            )
        }
        _units.value = dummyUnits
    }

    fun toggleUnitSelection(unitId: Int) {
        _units.update { units ->
            units.map { unit ->
                if (unit.id == unitId) {
                    unit.copy(isSelected = !unit.isSelected)
                } else {
                    unit
                }
            }
        }
    }

    fun selectUnit(unitId: Int) {
        _units.update { units ->
            units.map { unit ->
                if (unit.id == unitId) {
                    unit.copy(isSelected = true)
                } else {
                    unit
                }
            }
        }
    }

    fun unselectUnit(unitId: Int) {
        _units.update { units ->
            units.map { unit ->
                if (unit.id == unitId) {
                    unit.copy(isSelected = false)
                } else {
                    unit
                }
            }
        }
    }

    fun isUnitSelected(unitId: Int): Boolean {
        return _units.value.find { it.id == unitId }?.isSelected ?: false
    }

    // Qo'shimcha funksiyalar (ActivityHome'dan ilhomlanib)
    fun selectAll() {
        _units.update { units ->
            units.map { it.copy(isSelected = true) }
        }
    }

    fun clearAll() {
        _units.update { units ->
            units.map { it.copy(isSelected = false) }
        }
    }

    fun invertAll() {
        _units.update { units ->
            units.map { it.copy(isSelected = !it.isSelected) }
        }
    }
}