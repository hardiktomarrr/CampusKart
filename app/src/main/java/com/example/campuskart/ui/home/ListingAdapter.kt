package com.example.campuskart.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.campuskart.R
import com.example.campuskart.data.ListingEntity
import java.text.NumberFormat
import java.util.Locale

class ListingAdapter(private val onItemClick: (ListingEntity) -> Unit) :
    ListAdapter<ListingEntity, ListingAdapter.ListingViewHolder>(ListingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_listing, parent, false)
        return ListingViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ListingViewHolder(
        itemView: View,
        private val onItemClick: (ListingEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val ivImage: ImageView = itemView.findViewById(R.id.ivListingImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvCondition: TextView = itemView.findViewById(R.id.tvCondition)
        private val tvSoldBadge: TextView = itemView.findViewById(R.id.tvSoldBadge)

        fun bind(listing: ListingEntity) {
            tvTitle.text = listing.title
            val formattedPrice = NumberFormat.getNumberInstance(Locale("en", "IN")).format(listing.price)
            tvPrice.text = "Rs. $formattedPrice"
            tvCategory.text = listing.category
            tvCondition.text = listing.condition

            // Category Icon Badge
            val categoryIcon = when (listing.category) {
                "Electronics" -> R.drawable.ic_laptop_bg
                "Appliances" -> R.drawable.ic_couch_bg
                "Sports" -> R.drawable.ic_bike_bg
                else -> R.drawable.ic_book_bg
            }
            tvCategory.setCompoundDrawablesWithIntrinsicBounds(categoryIcon, 0, 0, 0)

            // Image Loader
            val context = itemView.context
            val imageResId = context.resources.getIdentifier(
                listing.imageResName,
                "drawable",
                context.packageName
            )

            if (imageResId != 0) {
                ivImage.setImageResource(imageResId)
            } else {
                ivImage.setImageResource(android.R.color.transparent)
                ivImage.setBackgroundColor(android.graphics.Color.parseColor("#E8EAF6"))
            }

            if (listing.isSold) {
                tvSoldBadge.visibility = View.VISIBLE
            } else {
                tvSoldBadge.visibility = View.GONE
            }

            itemView.setOnClickListener {
                onItemClick(listing)
            }
        }
    }

    class ListingDiffCallback : DiffUtil.ItemCallback<ListingEntity>() {
        override fun areItemsTheSame(oldItem: ListingEntity, newItem: ListingEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListingEntity, newItem: ListingEntity): Boolean {
            return oldItem == newItem
        }
    }
}