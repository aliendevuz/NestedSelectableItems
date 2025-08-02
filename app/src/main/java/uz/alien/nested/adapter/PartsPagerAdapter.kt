package uz.alien.nested.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.alien.nested.model.PartUIState
import uz.alien.nested.ui.UnitsFragment

class PartsPagerAdapter(
    fa: FragmentActivity,
    private val parts: List<PartUIState>
) : FragmentStateAdapter(fa) {
    override fun getItemCount() = parts.size
    override fun createFragment(position: Int): Fragment {
        return UnitsFragment.newInstance(parts[position])
    }
}