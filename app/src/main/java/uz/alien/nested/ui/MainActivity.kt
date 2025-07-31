package uz.alien.nested.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import uz.alien.nested.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        }
    }
}