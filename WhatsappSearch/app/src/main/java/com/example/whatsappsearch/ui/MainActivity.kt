package com.example.whatsappsearch.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.whatsappsearch.databinding.ActivityMainBinding
import com.example.whatsappsearch.viewmodel.StateViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var stateViewModel: StateViewModel
    private lateinit var stateAdapter: StateAdapter
    private var matchPositions = listOf<Int>()
    private var startMatchIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stateViewModel = ViewModelProvider(this)[StateViewModel::class.java]

        stateAdapter = StateAdapter()
        binding.rvStateList.layoutManager = LinearLayoutManager(this)
        binding.rvStateList.adapter = stateAdapter

        observeViewModel()
        stateViewModel.loadStateData()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                stateAdapter.setSearchQuery(query)

                val allItems = stateViewModel.stateList.value ?: emptyList()
                matchPositions = allItems.mapIndexedNotNull { index, item ->
                    if (
                        item.StateName.contains(query, ignoreCase = true) ||
                        item.Country .contains(query, ignoreCase = true)
                    ) index else null
                }

                startMatchIndex = -1
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (matchPositions.isEmpty()) {
                    Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }

        binding.btnDown.setOnClickListener {
            if (matchPositions.isNotEmpty()) {
                if (startMatchIndex + 1 < matchPositions.size) {
                    startMatchIndex++
                    val position = matchPositions[startMatchIndex]
                    binding.rvStateList.scrollToPosition(position)
                } else {
                    Toast.makeText(this, "No more matches found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No matches found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnUp.setOnClickListener {
            if (matchPositions.isNotEmpty()) {
                if (startMatchIndex - 1 >= 0) {
                    startMatchIndex--
                    val position = matchPositions[startMatchIndex]
                    binding.rvStateList.scrollToPosition(position)
                } else {
                    Toast.makeText(this, "No more matches found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No matches found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        stateViewModel.stateList.observe(this) {
            stateAdapter.setData(it)
        }


        stateViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        stateViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
