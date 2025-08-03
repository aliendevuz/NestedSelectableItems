package uz.alien.nested.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import uz.alien.nested.R
import uz.alien.nested.presentation.model.CollectionUIState

class CollectionAdapter(
    private val onClick: (Int) -> Unit
) : androidx.recyclerview.widget.ListAdapter<CollectionUIState, CollectionViewHolder>(CollectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind( getItem(position))
    }
}