package uz.alien.nested.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.alien.nested.adapter.PartAdapter
import uz.alien.nested.adapter.PartsPagerAdapter
import uz.alien.nested.databinding.ActivityMainBinding
import uz.alien.nested.utils.AutoLayoutManager
import uz.alien.nested.utils.MarginItemDecoration

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var partAdapter: PartAdapter
    private lateinit var partsPagerAdapter: PartsPagerAdapter

    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
    }

    private fun initViews() {

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
                    partsPagerAdapter = PartsPagerAdapter(this@MainActivity, parts)
                    binding.vpUnits.adapter = partsPagerAdapter
                    binding.vpUnits.offscreenPageLimit = parts.size
                }
                if (binding.rvParts.adapter == null) {
                    binding.rvParts.layoutManager = AutoLayoutManager(this@MainActivity, parts.size)
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

        binding.bSelect.setOnClickListener {
            viewModel.selectAll()
        }
        binding.bRandom.setOnClickListener {
            viewModel.randomSelect()
        }
        binding.bInvert.setOnClickListener {
            viewModel.invertAll()
        }
        binding.bClear.setOnClickListener {
            viewModel.clearAll()
        }

        binding.bStart.setOnClickListener {
            val selectedUnits = viewModel.getSelectedUnits()
            Log.d("@@@@", selectedUnits.joinToString { "Unit: ${it.partIndex}: ${it.unitId}" })
//            val intent = Intent(this, TestActivity::class.java)
//            startActivity(intent)
        }
    }
}