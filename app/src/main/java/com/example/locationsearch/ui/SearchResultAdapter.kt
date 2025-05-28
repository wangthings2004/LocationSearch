package com.example.locationsearch.ui

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.locationsearch.data.model.LocationResult
import com.example.locationsearch.databinding.ItemSearchResultBinding

class SearchResultAdapter :
    ListAdapter<LocationResult, SearchResultAdapter.SearchResultViewHolder>(LocationResultDiffCallback) {

    var onItemClick: ((LocationResult) -> Unit)? = null
    private var query: String = ""

    fun submitLimitedList(list: List<LocationResult>, newQuery: String) {
        query = newQuery
        submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val result = getItem(position)
        holder.bind(result, query)
    }

    inner class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: LocationResult, query: String) {
            binding.tvSearchResult.text = highlightKeyword(data.displayName, query)

            binding.root.setOnClickListener {
                onItemClick?.invoke(data)
            }
        }

        private fun highlightKeyword(text: String, keyword: String): SpannableString {
            val spannable = SpannableString(text)
            val start = text.indexOf(keyword, ignoreCase = true)
            if (start >= 0) {
                spannable.setSpan(
                    StyleSpan(Typeface.BOLD),
                    start,
                    start + keyword.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            return spannable
        }
    }

    companion object LocationResultDiffCallback : DiffUtil.ItemCallback<LocationResult>() {
        override fun areItemsTheSame(oldItem: LocationResult, newItem: LocationResult): Boolean {
            return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
        }

        override fun areContentsTheSame(oldItem: LocationResult, newItem: LocationResult): Boolean {
            return oldItem == newItem
        }
    }
}