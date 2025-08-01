package uz.alien.nested.adapter

import androidx.recyclerview.widget.DiffUtil
import uz.alien.nested.model.PartUIState

class PartDiffCallback : DiffUtil.ItemCallback<PartUIState>() {
    override fun areItemsTheSame(old: PartUIState, new: PartUIState) = old.id == new.id
    override fun areContentsTheSame(old: PartUIState, new: PartUIState): Boolean {
        return old == new
    }
}