package uz.alien.nested.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnitUIState(
    val id: Int,
    val name: String,
    val progress: Int,
    val part: Int,
    val isSelected: Boolean = false
) : Parcelable