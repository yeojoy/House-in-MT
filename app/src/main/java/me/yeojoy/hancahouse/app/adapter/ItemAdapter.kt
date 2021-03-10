package me.yeojoy.hancahouse.app.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.yeojoy.hancahouse.BuildConfig
import me.yeojoy.hancahouse.R
import me.yeojoy.hancahouse.app.Constants
import me.yeojoy.hancahouse.main.TabPageContract
import me.yeojoy.hancahouse.model.House
import me.yeojoy.hancahouse.model.Item
import java.text.SimpleDateFormat
import java.util.*

class ItemAdapter<T>(private val presenter: TabPageContract.Presenter<T>)
    : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_item, parent, false)
        return ItemViewHolder(view, presenter::itemClicked)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = presenter.itemAt(position)

        val thumbnailUrl: String?
        val title: String?
        var author: String?
        val date: Long?

        val uid: Long?

        when (item) {
            is Item -> {
                thumbnailUrl = item.thumbnailUrl
                title = item.title
                author = item.author
                date = item.date
                uid = item.uid
            }
            is House -> {
                thumbnailUrl = item.thumbnailUrl
                title = item.title
                author = item.author
                date = item.date
                uid = item.uid
            }
            else -> {
                thumbnailUrl = ""
                title = "No title"
                author = ""
                date = 0L
                uid = 999999L
            }
        }

        if (thumbnailUrl.isNullOrEmpty()) {
            holder.imageViewThumbnail.setImageResource(R.drawable.ic_home_black_24dp)
        } else {
            Glide.with(holder.itemView)
                    .load(thumbnailUrl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_home_black_24dp)
                    .into(holder.imageViewThumbnail)
        }

        val simpleDateFormat = SimpleDateFormat(Constants.APP_DATE_FORMAT, Locale.getDefault())
        holder.textViewTitle.text = title
        holder.textViewDate.text = simpleDateFormat.format(Date(date))

        author = if (BuildConfig.DEBUG) {
            "$author >>> uid : $uid"
        } else {
            author
        }

        holder.textViewAuthor.text = author
    }

    override fun getItemCount(): Int {
        return presenter.numberOfItem()
    }

    class ItemViewHolder(itemView: View, val itemClickListener: (position: Int) -> Unit)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        val imageViewThumbnail: ImageView = itemView.findViewById(R.id.image_view_thumbnail)
        val textViewTitle: TextView = itemView.findViewById(R.id.text_view_title)
        val textViewDate: TextView = itemView.findViewById(R.id.text_view_date)
        val textViewAuthor: TextView = itemView.findViewById(R.id.text_view_author)

        override fun onClick(v: View?) {
            itemClickListener(adapterPosition)
        }
    }
}
