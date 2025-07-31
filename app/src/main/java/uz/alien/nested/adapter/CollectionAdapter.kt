package uz.alien.nested.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.alien.nested.R
import uz.alien.nested.model.CollectionUIState

class CollectionAdapter(
    private val resources: Resources,
    private val onClick: (Int) -> Unit
) : ListAdapter<CollectionUIState, CollectionViewHolder>(CollectionDiffCallback()) {

    var selectedIndex: Int = 0
        set(value) {
            field = value
            notifyChangedItems()
        }

    var oldSelection = selectedIndex

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionViewHolder(view, resources, onClick)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
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