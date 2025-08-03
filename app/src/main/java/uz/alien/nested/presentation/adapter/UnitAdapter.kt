package uz.alien.nested.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.alien.nested.R
import uz.alien.nested.presentation.model.UnitUIState

class UnitAdapter : androidx.recyclerview.widget.ListAdapter<UnitUIState, UnitViewHolder>(UnitDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnitViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_unit, parent, false)
        return UnitViewHolder(view)
    }

    override fun onBindViewHolder(holder: UnitViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}