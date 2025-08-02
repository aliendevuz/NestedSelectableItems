package uz.alien.nested.ui

import android.os.Build
import android.os.Bundle
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

    private lateinit var unitAdapter: UnitAdapter
    private lateinit var part: PartUIState

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        part = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_PART, PartUIState::class.java)!!
        } else {
            arguments?.getParcelable(ARG_PART)!!
        }

        _binding = FragmentUnitsBinding.inflate(layoutInflater, container, false)

        initViews()

        return binding.root
    }

    private fun initViews() {

        unitAdapter = UnitAdapter()

        val spanCount = if (part.unitCount == 20) 2 else 3
        binding.rvSelectableUnits.layoutManager = AutoLayoutManager(requireContext(), spanCount)
        binding.rvSelectableUnits.addItemDecoration(
            MarginItemDecoration(3.2F, resources, part.unitCount, true)
        )
        binding.rvSelectableUnits.adapter = unitAdapter

        binding.rvSelectableUnits.selectListener = object : SelectableRecyclerView.OnUnitSelectListener {

            override fun onSingleTap(position: Int) {
                viewModel.toggleUnitSelection(position)
            }

            override fun onLongPress(position: Int) {
                binding.rvSelectableUnits.setSelection(viewModel.isUnitSelected(position))
                viewModel.toggleUnitSelection(position)
            }

            override fun onMove(position: Int, selection: Boolean) {
                if (selection) {
                    viewModel.selectUnit(position)
                } else {
                    viewModel.unselectUnit(position)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.unitFlows[part.collectionId][part.id].collectLatest { units ->
                unitAdapter.submitList(units)
            }
        }
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