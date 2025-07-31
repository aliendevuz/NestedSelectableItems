package uz.alien.nested.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.alien.nested.model.CollectionUIState
import uz.alien.nested.ui.PartsFragment

class CollectionsPagerAdapter(
    fa: FragmentActivity,
    private val collections: List<CollectionUIState>
) : FragmentStateAdapter(fa) {
    override fun getItemCount() = collections.size
    override fun createFragment(position: Int): Fragment {
        return PartsFragment.newInstance(collections[position])
    }
}