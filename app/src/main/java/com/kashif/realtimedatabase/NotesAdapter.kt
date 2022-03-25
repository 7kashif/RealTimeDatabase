package com.kashif.realtimedatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kashif.realtimedatabase.databinding.NoteItemBinding

class NotesAdapter: ListAdapter<NoteItem,NotesAdapter.NoteViewHolder>(diffUtil) {

    inner class NoteViewHolder(val binding: NoteItemBinding): RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<NoteItem>() {
            override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
                return oldItem.itemId==newItem.itemId
            }

            override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
                return oldItem==newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.apply {
            holder.itemView.apply {
                tvTitle.text = item.itemTitle
                tvBody.text = item.itemBody
                tvTime.text = Utils.formatDay(item.itemId)
            }
        }
    }

}