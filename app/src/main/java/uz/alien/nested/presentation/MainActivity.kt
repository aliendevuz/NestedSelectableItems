package uz.alien.nested.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.alien.nested.presentation.adapter.CollectionAdapter
import uz.alien.nested.presentation.adapter.CollectionsPagerAdapter
import uz.alien.nested.databinding.ActivityMainBinding
import uz.alien.nested.utils.AutoLayoutManager
import uz.alien.nested.utils.Logger
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

        initViews()
        setupButtons()
    }

    private fun initViews() {
        collectionAdapter = CollectionAdapter { selectedIndex ->
            viewModel.setCurrentCollection(selectedIndex)
            binding.vpParts.currentItem = selectedIndex
        }

        val collections = viewModel.collectionsFlow.value

        binding.rvCollections.layoutManager =
            AutoLayoutManager(this, collections.size)
        binding.rvCollections.addItemDecoration(
            MarginItemDecoration(3.2f, resources, collections.size, true)
        )
        binding.rvCollections.adapter = collectionAdapter

        collectionsPagerAdapter = CollectionsPagerAdapter(this, collections)
        binding.vpParts.adapter = collectionsPagerAdapter

        binding.vpParts.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                viewModel.setCurrentCollection(position)
            }
        })

        var isFirst = true

        lifecycleScope.launch {
            viewModel.collectionsFlow.collectLatest { collections ->
                collectionAdapter.submitList(collections)
                if (isFirst) {
                    binding.vpParts.offscreenPageLimit = collections.size
                    isFirst = false
                }
            }
        }
    }

    private fun setupButtons() {

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
            Logger.d("MainActivity", selectedUnits.joinToString { "Unit: ${it.collectionId}:${it.partId}:${it.unitId}" })
        }
    }
}