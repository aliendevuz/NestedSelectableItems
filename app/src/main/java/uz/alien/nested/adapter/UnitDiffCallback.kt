package uz.alien.nested.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.alien.nested.ui.UnitUIState

class UnitDiffCallback : DiffUtil.ItemCallback<UnitUIState>() {
    override fun areItemsTheSame(old: UnitUIState, new: UnitUIState) = old.id == new.id
    override fun areContentsTheSame(old: UnitUIState, new: UnitUIState) = old == new
}