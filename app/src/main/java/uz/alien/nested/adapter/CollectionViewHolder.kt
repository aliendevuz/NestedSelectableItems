package uz.alien.nested.adapter

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uz.alien.nested.R
import uz.alien.nested.databinding.ItemCollectionBinding
import uz.alien.nested.model.CollectionUIState
import uz.alien.nested.model.PartUIState

class CollectionViewHolder(
    itemView: View,
    private val resources: Resources,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemCollectionBinding.bind(itemView)

    fun bind(collection: CollectionUIState, isSelected: Boolean) {
        binding.tvCollectionNumber.text = collection.title

        val isAnyUnitSelected = collection.parts.any { it.units.any { it1 -> it1.isSelected } }

        val backgroundRes = when {
            isSelected && isAnyUnitSelected -> R.drawable.background_collection_selected_current
            isAnyUnitSelected -> R.drawable.background_collection_selected
            isSelected -> R.drawable.background_collection_current
            else -> R.drawable.background_collection_default
        }

        itemView.setBackgroundResource(backgroundRes)

        binding.tvCollectionNumber.setTextColor(
            resources.getColor(
                if (isSelected) R.color.color_background else R.color.primary_color,
                resources.newTheme()
            )
        )

        itemView.setOnClickListener {
            onClick(collection.index)
        }
    }
}