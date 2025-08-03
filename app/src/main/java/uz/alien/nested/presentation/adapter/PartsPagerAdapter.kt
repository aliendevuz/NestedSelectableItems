package uz.alien.nested.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.alien.nested.presentation.model.PartUIState
import uz.alien.nested.presentation.UnitsFragment

class PartsPagerAdapter(
    fa: FragmentActivity,
    private val parts: List<PartUIState>
) : androidx.viewpager2.adapter.FragmentStateAdapter(fa) {
    override fun getItemCount() = parts.size
    override fun createFragment(position: Int): Fragment {
        return UnitsFragment.newInstance(parts[position])
    }
}