package uz.alien.nested.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.alien.nested.adapter.PartAdapter
import uz.alien.nested.adapter.PartsPagerAdapter
import uz.alien.nested.databinding.FragmentPartsBinding
import uz.alien.nested.model.CollectionUIState
import uz.alien.nested.utils.AutoLayoutManager
import uz.alien.nested.utils.MarginItemDecoration

class PartsFragment : Fragment() {

    private var _binding: FragmentPartsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var partAdapter: PartAdapter
    private lateinit var partsPagerAdapter: PartsPagerAdapter
    private var collection: CollectionUIState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        collection = arguments?.getParcelable(ARG_COLLECTION)
        _binding = FragmentPartsBinding.inflate(layoutInflater, container, false)

        partAdapter = PartAdapter(resources) { selectedIndex ->
            viewModel.setSelectedPart(selectedIndex)
        }

        binding.vpUnits.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setSelectedPart(position)
            }
        })

        lifecycleScope.launch {
            viewModel.parts.collectLatest { parts ->
                partAdapter.submitList(parts)
                if (binding.vpUnits.adapter == null) {
                    partsPagerAdapter = PartsPagerAdapter(requireActivity(), parts)
                    binding.vpUnits.adapter = partsPagerAdapter
                    binding.vpUnits.offscreenPageLimit = parts.size
                }
                if (binding.rvParts.adapter == null) {
                    binding.rvParts.layoutManager = AutoLayoutManager(requireContext(), parts.size)
                    binding.rvParts.addItemDecoration(
                        MarginItemDecoration(
                            3.2F,
                            resources,
                            parts.size,
                            true
                        )
                    )
                    binding.rvParts.adapter = partAdapter
                }
            }
        }

        lifecycleScope.launch {
            var isFirst = true
            viewModel.selectedPartIndex.collect { selectedIndex ->
                partAdapter.selectedIndex = selectedIndex
                binding.vpUnits.setCurrentItem(selectedIndex, !isFirst)
                isFirst = false
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_COLLECTION = "arg_collection"
        fun newInstance(collection: CollectionUIState): PartsFragment {
            val fragment = PartsFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_COLLECTION, collection)
            fragment.arguments = bundle
            return fragment
        }
    }
}