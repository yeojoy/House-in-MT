package me.yeojoy.hancahouse.app.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import me.yeojoy.hancahouse.BuildConfig
import me.yeojoy.hancahouse.MainContract
import me.yeojoy.hancahouse.R
import me.yeojoy.hancahouse.app.Constants
import java.text.SimpleDateFormat
import java.util.*

class HouseAdapter(private val presenter: MainContract.Presenter)
    : RecyclerView.Adapter<HouseAdapter.HouseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_house, parent, false)
        return HouseViewHolder(view, presenter::houseItemClicked)
    }

    override fun onBindViewHolder(holder: HouseViewHolder, position: Int) {
        val house = presenter.houseAt(position)
        if (house.thumbnailUrl.isNotEmpty() && !house.thumbnailUrl.equals(Constants.NO_IMAGE)) {
            Glide.with(holder.itemView)
                    .load(house.thumbnailUrl)
                    .fitCenter()
                    .placeholder(R.drawable.ic_home_black_24dp)
                    .into(holder.imageViewThumbnail)
        } else {
            holder.imageViewThumbnail.setImageResource(R.drawable.ic_home_black_24dp)
        }

        val simpleDateFormat = SimpleDateFormat(Constants.APP_DATE_FORMAT, Locale.getDefault())
        holder.textViewTitle.text = house.title
        holder.textViewDate.text = simpleDateFormat.format(Date(house.date))

        val author = if (BuildConfig.DEBUG) {
            "${house.author} >>> uid : ${house.uid.toString()}"
        } else {
            house.author
        }

        holder.textViewAuthor.text = author
    }

    override fun getItemCount(): Int {
        return presenter.numberOfHouses()
    }

    class HouseViewHolder(itemView: View, val itemClickListener: (position: Int) -> Unit)
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
