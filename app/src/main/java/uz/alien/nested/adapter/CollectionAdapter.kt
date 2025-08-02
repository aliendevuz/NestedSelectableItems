package uz.alien.nested.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import uz.alien.nested.R
import uz.alien.nested.model.CollectionUIState

class CollectionAdapter(
    private val onClick: (Int) -> Unit
) : ListAdapter<CollectionUIState, CollectionViewHolder>(CollectionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_collection, parent, false)
        return CollectionViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind( getItem(position))
    }
}