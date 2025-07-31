package uz.alien.nested.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartUIState(
    val index: Int,
    val title: String,
    val units: List<UnitUIState>
): Parcelable