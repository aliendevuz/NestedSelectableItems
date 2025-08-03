package uz.alien.nested.presentation.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.alien.nested.R
import uz.alien.nested.databinding.ItemCollectionBinding
import uz.alien.nested.presentation.model.CollectionUIState

class CollectionViewHolder(
    itemView: View,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemCollectionBinding.bind(itemView)

    fun bind(collection: CollectionUIState) {

        val isSelected = collection.isSelected
        val isCurrent = collection.isCurrent

        binding.tvCollectionNumber.text = collection.title

        val backgroundRes = when {
            isCurrent && isSelected -> R.drawable.background_collection_selected_current
            isSelected -> R.drawable.background_collection_selected
            isCurrent -> R.drawable.background_collection_current
            else -> R.drawable.background_collection_default
        }

        itemView.setBackgroundResource(backgroundRes)

        binding.tvCollectionNumber.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                if (isCurrent) R.color.color_background else R.color.primary_color
            )
        )

        itemView.setOnClickListener {
            onClick(collection.id)
        }
    }
}