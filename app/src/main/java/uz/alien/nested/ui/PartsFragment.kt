package uz.alien.nested.ui

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.alien.nested.adapter.PartAdapter
import uz.alien.nested.adapter.PartsPagerAdapter
import uz.alien.nested.databinding.FragmentPartsBinding
import uz.alien.nested.model.CollectionUIState
import uz.alien.nested.utils.AutoLayoutManager
import uz.alien.nested.utils.Logger
import uz.alien.nested.utils.MarginItemDecoration

class PartsFragment : Fragment() {

    private var _binding: FragmentPartsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    private lateinit var partAdapter: PartAdapter
    private lateinit var partsPagerAdapter: PartsPagerAdapter
    private lateinit var collection: CollectionUIState

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_COLLECTION, CollectionUIState::class.java)!!
        } else {
            arguments?.getParcelable(ARG_COLLECTION)!!
        }

        _binding = FragmentPartsBinding.inflate(layoutInflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {

        partAdapter = PartAdapter { selectedIndex ->
            viewModel.setCurrentPart(selectedIndex)
            binding.vpUnits.currentItem = selectedIndex
        }

        val parts = viewModel.partsFlows[collection.id]

        binding.rvParts.layoutManager = AutoLayoutManager(requireContext(), parts.value.size)
        binding.rvParts.addItemDecoration(
            MarginItemDecoration(3.2F, resources, parts.value.size, true)
        )
        binding.rvParts.adapter = partAdapter

        partsPagerAdapter = PartsPagerAdapter(requireActivity(), parts.value)
        binding.vpUnits.adapter = partsPagerAdapter

        binding.vpUnits.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentPart(collection.id, position)
            }
        })

        var isFirst = true

        lifecycleScope.launch {
            viewModel.partsFlows[collection.id].collectLatest { parts ->
                partAdapter.submitList(parts)
                if (isFirst) {
                    delay(50L)
                    binding.vpUnits.offscreenPageLimit = collection.partCount
                    isFirst = false
                }
            }
        }
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