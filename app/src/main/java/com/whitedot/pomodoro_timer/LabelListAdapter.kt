package com.whitedot.pomodoro_timer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.whitedot.pomodoro_timer.data.Label

class LabelListAdapter : ListAdapter<Label, LabelListAdapter.LabelViewHolder>(LabelComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelViewHolder {
        return LabelViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: LabelViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current.label)
    }

    class LabelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val labelItemView: TextView = itemView.findViewById(R.id.recyclerview_item_label_name)

        fun bind(text: String?) {
            labelItemView.text = text
        }

        companion object {
            fun create(parent: ViewGroup): LabelViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.recyclerview_label_item, parent, false)
                return LabelViewHolder(view)
            }
        }
    }

    class LabelComparator : DiffUtil.ItemCallback<Label>() {
        override fun areItemsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Label, newItem: Label): Boolean {
            return oldItem.label == newItem.label
        }

    }
}