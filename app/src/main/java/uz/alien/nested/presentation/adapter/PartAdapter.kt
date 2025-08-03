package uz.alien.nested.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.alien.nested.R
import uz.alien.nested.presentation.model.PartUIState

class PartAdapter(
    private val onClick: (Int) -> Unit
) : androidx.recyclerview.widget.ListAdapter<PartUIState, PartViewHolder>(PartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent, false)
        return PartViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        holder.bind( getItem(position))
    }
}