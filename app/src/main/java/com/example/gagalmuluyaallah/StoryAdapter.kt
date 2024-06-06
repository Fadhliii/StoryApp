import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.mycamera.withDateFormat
import com.example.gagalmuluyaallah.databinding.ItemStoryBinding
import com.example.gagalmuluyaallah.response.StoryItem


//!!==================================================!!//
//class StoryAdapter : PagingDataAdapter<StoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
//
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding = ItemStoryBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//        )
//
//        return ListViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val storyItem = getItem(position)
//        if (storyItem != null) {
//            holder.bind(storyItem)
//        }
//    }
//
//    inner class ListViewHolder(private var binding: ItemStoryBinding)
//        : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: StoryItem?) {
//            binding.apply {
//                storyName.text = item?.name
//                storyDescription.text = item?.description
//                storyDate.text = item?.createdAt?.withDateFormat()
//                Glide.with(itemView.context)
//                    .load(item?.photoUrl)
//                    .into(storyImage)
//
//                itemView.setOnClickListener {
//                    onItemClickCallback.onItemClicked(item)
//                }
//            }
//        }
//    }
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(items: StoryItem?)
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
//            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}
//!!==================================================!!//
//class StoryAdapter : PagingDataAdapter<StoryItem, StoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
//
//    private lateinit var onItemClickCallback: OnItemClickCallback
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding = ItemStoryBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//        )
//
//        return ListViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        val storyItem = getItem(position)
//        if (storyItem != null) {
//            holder.bind(storyItem)
//        }
//    }
//
//    inner class ListViewHolder(private var binding: ItemStoryBinding)
//        : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: StoryItem?) {
//            binding.apply {
//                storyName.text = item?.name
//                storyDescription.text = item?.description
//                storyDate.text = item?.createdAt?.withDateFormat()
//                Glide.with(itemView.context)
//                    .load(item?.photoUrl)
//                    .into(storyImage)
//
//                itemView.setOnClickListener {
//                    onItemClickCallback.onItemClicked(item)
//                }
//            }
//        }
//    }
//
//    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback
//    }
//
//    interface OnItemClickCallback {
//        fun onItemClicked(items: StoryItem?)
//    }
//
//    companion object {
//        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
//            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
//                return oldItem.id == newItem.id
//            }
//
//            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
//                return oldItem == newItem
//            }
//        }
//    }
//}
//!=======   nomor 3       ===========================================!!
class StoryAdapter : RecyclerView.Adapter<StoryAdapter.ListViewHolder>() {

    private var storyList: List<StoryItem> = listOf()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setData(storyList: List<StoryItem>) {
        this.storyList = storyList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )

        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val storyItem = storyList[position]
        holder.bind(storyItem)
    }

    override fun getItemCount(): Int = storyList.size

    inner class ListViewHolder(private var binding: ItemStoryBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StoryItem) {
            binding.apply {
                storyName.text = item.name
                storyDescription.text = item.description
                storyDate.text = item.createdAt?.withDateFormat()
                Glide.with(itemView.context)
                    .load(item.photoUrl)
                    .into(storyImage)

                itemView.setOnClickListener {
                    onItemClickCallback.onItemClicked(item)
                }
            }
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(items: StoryItem)
    }
}