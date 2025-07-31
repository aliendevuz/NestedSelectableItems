package uz.alien.nested.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.alien.nested.model.CollectionUIState

class CollectionDiffCallback : DiffUtil.ItemCallback<CollectionUIState>() {
    override fun areItemsTheSame(old: CollectionUIState, new: CollectionUIState) = old.index == new.index
    override fun areContentsTheSame(old: CollectionUIState, new: CollectionUIState): Boolean {
        val oldSelectedUnits = old.parts.count { it.units.count { it1 -> it1.isSelected } == 0 } == 0
        val newSelectedUnits = new.parts.count { it.units.count { it1 -> it1.isSelected } == 0 } == 0
        return (old.title == new.title) && oldSelectedUnits == newSelectedUnits
    }
}