package ru.itrequest.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import ru.itrequest.R
import ru.itrequest.data.Waterfall

class WaterfallAdapter(private val onClick: (item: Waterfall) -> Unit) :
    ListAdapter<Waterfall, WaterfallAdapter.WaterfallViewHolder>(WaterfallDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaterfallViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.waterfall_element, parent, false)
        return WaterfallViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: WaterfallViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class WaterfallViewHolder(itemView: View, private val onClick: (item: Waterfall) -> Unit)
        : RecyclerView.ViewHolder(itemView) {

        private val topLabel = itemView.findViewById<MaterialTextView>(R.id.topLabel)
        private val descriptionLabel = itemView.findViewById<MaterialTextView>(R.id.descriptionLabel)

        // Suppress for quickly resolving
        @SuppressLint("SetTextI18n")
        fun bind(item: Waterfall) {
            topLabel.text = item.name
            descriptionLabel.text = "Address: ${item.address}"
            itemView.setOnClickListener {
                onClick(item)
            }
        }
    }

    object WaterfallDiff: DiffUtil.ItemCallback<Waterfall>() {
        override fun areItemsTheSame(oldItem: Waterfall, newItem: Waterfall): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Waterfall, newItem: Waterfall): Boolean {
            return oldItem == newItem
        }

    }
}



