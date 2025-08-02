package uz.alien.nested.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.alien.nested.model.CollectionUIState

class CollectionDiffCallback : DiffUtil.ItemCallback<CollectionUIState>() {
    override fun areItemsTheSame(old: CollectionUIState, new: CollectionUIState) = old.id == new.id
    override fun areContentsTheSame(old: CollectionUIState, new: CollectionUIState): Boolean {
        return old.isSelected == new.isSelected && old.isCurrent == new.isCurrent
    }
}