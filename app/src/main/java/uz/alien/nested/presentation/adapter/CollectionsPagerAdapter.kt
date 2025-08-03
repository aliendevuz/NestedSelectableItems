package uz.alien.nested.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import uz.alien.nested.presentation.model.CollectionUIState
import uz.alien.nested.presentation.PartsFragment

class CollectionsPagerAdapter(
    fa: FragmentActivity,
    private val collections: List<CollectionUIState>
) : androidx.viewpager2.adapter.FragmentStateAdapter(fa) {
    override fun getItemCount() = collections.size
    override fun createFragment(position: Int): Fragment {
        return PartsFragment.newInstance(collections[position])
    }
}