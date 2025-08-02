package uz.alien.nested.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.alien.nested.R
import uz.alien.nested.databinding.ItemPartBinding
import uz.alien.nested.model.PartUIState

class PartViewHolder(
    itemView: View,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemPartBinding.bind(itemView)

    fun bind(part: PartUIState) {

        val isSelected = part.isSelected
        val isCurrent = part.isCurrent

        binding.tvPartNumber.text = part.title

        val backgroundRes = when {
            isSelected && isCurrent -> R.drawable.background_part_selected_current
            isCurrent -> R.drawable.background_part_current
            isSelected -> R.drawable.background_part_selected
            else -> R.drawable.background_part_default
        }

        itemView.setBackgroundResource(backgroundRes)

        binding.tvPartNumber.setTextColor(
            ContextCompat.getColor(
                itemView.context,
                if (isCurrent) R.color.color_background else R.color.primary_color
            )
        )

        itemView.setOnClickListener {
            onClick(part.id)
        }
    }
}