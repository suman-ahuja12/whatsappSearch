package com.example.whatsappsearch.ui

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappsearch.databinding.ItemStateBinding
import com.example.whatsappsearch.model.StateDataItem

class StateAdapter : RecyclerView.Adapter<StateAdapter.StateViewHolder>() {

    private var mainList: List<StateDataItem> = emptyList()
    private var searchQuery: String = ""

    fun setData(filterList: List<StateDataItem>) {
        mainList = filterList
        notifyDataSetChanged()
    }

    fun setSearchQuery(query: String) {
        searchQuery = query
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StateViewHolder {
        val binding = ItemStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StateViewHolder, position: Int) {
        holder.bind(mainList[position])
    }

    override fun getItemCount(): Int = mainList.size

    inner class StateViewHolder(private val binding: ItemStateBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StateDataItem) {
            binding.tvState.text = getTextMatches(item.StateName, searchQuery)
            binding.tvCountry.text = getTextMatches(item.Country, searchQuery)

            Log.e("StateData", item.StateName)
        }

        private fun getTextMatches(originalText: String, query: String): SpannableString {
            val spannable = SpannableString(originalText)
            if (query.isEmpty()) return spannable

            val tempOriginal = originalText.lowercase()
            val tempQuery = query.lowercase()
            var startIndex = 0

            while (startIndex < tempOriginal.length) {
                val index = tempOriginal.indexOf(tempQuery, startIndex)
                if (index == -1) break

                spannable.setSpan(
                    BackgroundColorSpan(Color.YELLOW),
                    index,
                    index + query.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                startIndex = index + query.length
            }

            return spannable
        }

    }
}
