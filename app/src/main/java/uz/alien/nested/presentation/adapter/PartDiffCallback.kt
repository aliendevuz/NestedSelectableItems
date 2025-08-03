package uz.alien.nested.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.alien.nested.presentation.model.PartUIState

class PartDiffCallback : DiffUtil.ItemCallback<PartUIState>() {
    override fun areItemsTheSame(old: PartUIState, new: PartUIState) = old.id == new.id
    override fun areContentsTheSame(old: PartUIState, new: PartUIState): Boolean {
        return old.isSelected == new.isSelected && old.isCurrent == new.isCurrent
    }
}