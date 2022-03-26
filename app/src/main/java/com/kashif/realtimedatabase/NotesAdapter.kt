package com.kashif.realtimedatabase

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kashif.realtimedatabase.databinding.NoteItemBinding

class NotesAdapter : ListAdapter<NoteItem, NotesAdapter.NoteViewHolder>(diffUtil) {

    inner class NoteViewHolder(val binding: NoteItemBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<NoteItem>() {
            override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
                return oldItem.itemId == newItem.itemId
            }

            override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
                return oldItem == newItem
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
                if (item.done)
                    cLayout.setBackgroundColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.teal_200,
                            null
                        )
                    )
                else
                    cLayout.setBackgroundColor(
                        ResourcesCompat.getColor(
                            resources,
                            R.color.white,
                            null
                        )
                    )
                ibDelete.setOnClickListener {
                    clickListener?.let {
                        it(item)
                    }
                }
                root.setOnLongClickListener {
                    longClickListener?.let {
                        it(item)
                    }
                    true
                }
            }
        }
    }

    private var longClickListener: ((NoteItem) -> Unit)? = null
    private var clickListener: ((NoteItem) -> Unit)? = null
    fun onDeleteClickListener(listener: (NoteItem) -> Unit) {
        clickListener = listener
    }
    fun onLongClickListener(listener: (NoteItem) -> Unit) {
        longClickListener = listener
    }

}