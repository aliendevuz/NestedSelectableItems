package uz.alien.nested.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.alien.nested.R
import uz.alien.nested.ui.PartUIState

class PartAdapter(
    private val resources: Resources,
    private val onClick: (Int) -> Unit
) : ListAdapter<PartUIState, PartViewHolder>(PartDiffCallback()) {

    var selectedIndex: Int = 0
        set(value) {
            field = value
            notifyChangedItems()
        }

    var oldSelection = selectedIndex

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_part, parent, false)
        return PartViewHolder(view, resources, onClick)
    }

    override fun onBindViewHolder(holder: PartViewHolder, position: Int) {
        holder.bind( getItem(position), position == selectedIndex)
    }

    private fun notifyChangedItems() {
        if (selectedIndex != oldSelection) {
            notifyItemChanged(oldSelection)
            notifyItemChanged(selectedIndex)
            oldSelection = selectedIndex
        }
    }
}
