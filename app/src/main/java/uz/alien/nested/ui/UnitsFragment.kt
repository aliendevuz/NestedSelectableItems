package uz.alien.nested.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.alien.nested.adapter.UnitAdapter
import uz.alien.nested.databinding.FragmentUnitsBinding
import uz.alien.nested.model.PartUIState
import uz.alien.nested.utils.AutoLayoutManager
import uz.alien.nested.utils.MarginItemDecoration
import uz.alien.nested.utils.SelectableRecyclerView

class UnitsFragment : Fragment() {

    private var _binding: FragmentUnitsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: UnitAdapter
    private var part: PartUIState? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        part = arguments?.getParcelable(ARG_PART)
        _binding = FragmentUnitsBinding.inflate(layoutInflater, container, false)

        adapter = UnitAdapter()
        binding.rvSelectableUnits.layoutManager = AutoLayoutManager(requireContext(), 3)
        binding.rvSelectableUnits.adapter = adapter
        binding.rvSelectableUnits.addItemDecoration(
            MarginItemDecoration(
                3.2F,
                resources,
                3,
                true
            )
        )

        binding.rvSelectableUnits.selectListener = object : SelectableRecyclerView.OnUnitSelectListener {
            override fun onSingleTap(position: Int) {
                viewModel.toggleUnitSelection(position)
                Log.d("@@@@", "part: ${viewModel.selectedPartIndex.value}")
                Log.d("@@@@", "unit: $position")
            }

            override fun onLongPress(position: Int) {
                val isSelected = viewModel.isUnitSelected(position)
                if (isSelected) {
                    viewModel.unselectUnit(position)
                } else {
                    viewModel.selectUnit(position)
                }
                Log.d("@@@@", "part: ${viewModel.selectedPartIndex.value}")
                Log.d("@@@@", "unit: $position")
                binding.rvSelectableUnits.setSelection(isSelected)
            }

            override fun onMove(position: Int, selection: Boolean) {
                Log.d("@@@@", "part: ${viewModel.selectedPartIndex.value}")
                Log.d("@@@@", "unit: $position")
                if (selection) {
                    viewModel.selectUnit(position)
                } else {
                    viewModel.unselectUnit(position)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.parts.collectLatest { parts ->
                val currentPart = parts.find { it.index == part?.index }
                currentPart?.units?.let { adapter.submitList(it) }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PART = "arg_part"

        fun newInstance(part: PartUIState): UnitsFragment {
            val fragment = UnitsFragment()
            val bundle = Bundle()
            bundle.putParcelable(ARG_PART, part)
            fragment.arguments = bundle
            return fragment
        }
    }
}