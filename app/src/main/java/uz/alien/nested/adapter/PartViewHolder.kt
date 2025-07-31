package uz.alien.nested.adapter

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uz.alien.nested.R
import uz.alien.nested.databinding.ItemPartBinding
import uz.alien.nested.model.PartUIState

class PartViewHolder(
    itemView: View,
    private val resources: Resources,
    private val onClick: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemPartBinding.bind(itemView)

    fun bind(part: PartUIState, isSelected: Boolean) {
        binding.tvPartNumber.text = part.title

        val isAnyUnitSelected = part.units.any { it.isSelected }

        val backgroundRes = when {
            isSelected && isAnyUnitSelected -> R.drawable.background_part_selected_current
            isAnyUnitSelected -> R.drawable.background_part_selected
            isSelected -> R.drawable.background_part_current
            else -> R.drawable.background_part_default
        }

        itemView.setBackgroundResource(backgroundRes)

        binding.tvPartNumber.setTextColor(
            resources.getColor(
                if (isSelected) R.color.color_background else R.color.primary_color,
                resources.newTheme()
            )
        )

        itemView.setOnClickListener {
            onClick(part.index)
        }
    }
}