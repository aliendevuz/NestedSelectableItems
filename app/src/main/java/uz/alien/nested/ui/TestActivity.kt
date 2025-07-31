package uz.alien.nested.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.alien.nested.adapter.UnitAdapter
import uz.alien.nested.databinding.FragmentUnitsBinding
import uz.alien.nested.utils.AutoLayoutManager
import uz.alien.nested.utils.MarginItemDecoration
import uz.alien.nested.utils.SelectableRecyclerView

class TestActivity : AppCompatActivity() {

    private lateinit var binding: FragmentUnitsBinding
    private val viewModel: TestViewModel by viewModels()
    private lateinit var adapter: UnitAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUnitsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }


    private fun initViews() {
        adapter = UnitAdapter()
        binding.rvSelectableUnits.layoutManager = AutoLayoutManager(this, 3)
        binding.rvSelectableUnits.adapter = adapter

        binding.rvSelectableUnits.selectListener = object : SelectableRecyclerView.OnUnitSelectListener {
            override fun onSingleTap(position: Int) {
                viewModel.toggleUnitSelection(position)
            }

            override fun onLongPress(position: Int) {
                val isSelected = viewModel.isUnitSelected(position)
                if (isSelected) {
                    viewModel.unselectUnit(position)
                } else {
                    viewModel.selectUnit(position)
                }
                binding.rvSelectableUnits.setSelection(isSelected)
            }

            override fun onMove(position: Int, selection: Boolean) {
                if (selection) {
                    viewModel.selectUnit(position)
                } else {
                    viewModel.unselectUnit(position)
                }
            }
        }

        binding.rvSelectableUnits.addItemDecoration(
            MarginItemDecoration(
                spacing = 3.2f,
                resources = resources,
                spanCount = 3,
                includeEdge = true
            )
        )

        lifecycleScope.launch {
            viewModel.units.collectLatest { units ->
                adapter.submitList(units)
            }
        }
    }
}