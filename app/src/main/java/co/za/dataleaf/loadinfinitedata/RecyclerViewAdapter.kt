package co.za.dataleaf.loadinfinitedata

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import co.za.dataleaf.loadinfinitedata.databinding.RowItemBinding

data class InfiniteHolder(
    val id: Long,
    val url: String,
    val name: String
)
class RecyclerViewAdapter(val clickListener: (InfiniteHolder) -> Unit):
    ListAdapter<InfiniteHolder, RecyclerViewAdapter.ItemViewHolder>(DiffCallback) {

    private val _items: MutableList<InfiniteHolder>? = arrayListOf()

    class ItemViewHolder(
        private val binding: RowItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InfiniteHolder) {
            binding.tvItem.text = item.name
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<InfiniteHolder>() {
        override fun areItemsTheSame(oldItem: InfiniteHolder, newItem: InfiniteHolder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: InfiniteHolder, newItem: InfiniteHolder): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            RowItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        // Bind the ite
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            if (current != null) {
                clickListener(current)
            }
        }

        if (current != null) {
            holder.bind(current)
        }
    }

    fun getListItem(position: Int): InfiniteHolder? {
        return if(position <= 0) {
            null
        } else if (currentList.size >= position) {
            currentList[currentList.size -1]
        } else currentList[position]
    }

    fun setData(list: MutableList<InfiniteHolder>?) {
        if (list != null) {
            _items?.addAll(list)
        }
    }

    fun showData(): MutableList<InfiniteHolder>? {
        return _items
    }
}