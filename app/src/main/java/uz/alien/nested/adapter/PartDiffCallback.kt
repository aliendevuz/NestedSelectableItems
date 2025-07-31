package uz.alien.nested.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.alien.nested.model.PartUIState

class PartDiffCallback : DiffUtil.ItemCallback<PartUIState>() {
    override fun areItemsTheSame(old: PartUIState, new: PartUIState) = old.index == new.index
    override fun areContentsTheSame(old: PartUIState, new: PartUIState): Boolean {
        val oldSelectedUnits = old.units.count { it.isSelected } == 0
        val newSelectedUnits = new.units.count { it.isSelected } == 0
        return (old.title == new.title) && oldSelectedUnits == newSelectedUnits
    }
}