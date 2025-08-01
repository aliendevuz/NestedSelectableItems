package uz.alien.nested.ui

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
import uz.alien.nested.adapter.CollectionAdapter
import uz.alien.nested.adapter.CollectionsPagerAdapter
import uz.alien.nested.databinding.ActivityMainBinding
import uz.alien.nested.utils.AutoLayoutManager
import uz.alien.nested.utils.MarginItemDecoration

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()
    private lateinit var collectionAdapter: CollectionAdapter
    private lateinit var collectionsPagerAdapter: CollectionsPagerAdapter


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

//        collectionAdapter = CollectionAdapter(resources) { selectedIndex ->
//            viewModel.setSelectedCollection(selectedIndex)
//        }

//        binding.vpParts.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                viewModel.setSelectedCollection(position)
//            }
//        })

//        lifecycleScope.launch {
//            viewModel.collections.collectLatest { collections ->
//                collectionAdapter.submitList(collections)
//                if (binding.vpParts.adapter == null) {
//                    collectionsPagerAdapter = CollectionsPagerAdapter(this@MainActivity, collections)
//                    binding.vpParts.adapter = collectionsPagerAdapter
//                    binding.vpParts.offscreenPageLimit = 2
//                }
//                if (binding.rvCollections.adapter == null) {
//                    binding.rvCollections.layoutManager = AutoLayoutManager(this@MainActivity, 2)
//                    binding.rvCollections.addItemDecoration(
//                        MarginItemDecoration(3.2F, resources, collections.size, true)
//                    )
//                    binding.rvCollections.adapter = collectionAdapter
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            var isFirst = true
//            viewModel.selectedCollectionIndex.collect { selectedInex ->
//                collectionAdapter.selectedIndex = selectedInex
//                binding.vpParts.setCurrentItem(selectedInex, !isFirst)
//                isFirst = false
//            }
//        }

        setupButtons()
    }

    private fun setupButtons() {
//        binding.bSelect.setOnClickListener { viewModel.selectAll() }
//        binding.bRandom.setOnClickListener { viewModel.randomSelect() }
//        binding.bInvert.setOnClickListener { viewModel.invertAll() }
//        binding.bClear.setOnClickListener { viewModel.clearAll() }

        binding.bStart.setOnClickListener {
            val selectedUnits = viewModel.getSelectedUnits()
            Log.d("@@@@", selectedUnits.joinToString { "Unit: ${it.collectionId}:${it.partId}:${it.unitId}" })
        }
    }
}