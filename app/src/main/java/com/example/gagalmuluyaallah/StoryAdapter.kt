import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mycamera.withDateFormat
import com.example.gagalmuluyaallah.databinding.ItemStoryBinding
import com.example.gagalmuluyaallah.response.StoriesItemsResponse


////!!==================================================!!//

class StoryAdapter : PagingDataAdapter<StoriesItemsResponse, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private var onItemClickCallback: OnItemClickCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(items: StoriesItemsResponse?)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoriesItemsResponse) {
            binding.apply {
                storyName.text = item.name
                storyDescription.text = item.description
                storyDate.text = item.createdAt?.withDateFormat()
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(storyImage)

                itemView.setOnClickListener {
                    onItemClickCallback?.onItemClicked(item)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoriesItemsResponse>() {
            override fun areItemsTheSame(oldItem: StoriesItemsResponse, newItem: StoriesItemsResponse): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoriesItemsResponse, newItem: StoriesItemsResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}