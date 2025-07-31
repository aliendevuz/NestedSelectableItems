package uz.alien.nested.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CollectionUIState(
    val index: Int,
    val title: String,
    val parts: List<PartUIState>
): Parcelable