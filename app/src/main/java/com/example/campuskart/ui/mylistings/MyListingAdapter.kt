package com.example.campuskart.ui.mylistings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.campuskart.R
import com.example.campuskart.data.ListingEntity
import java.text.NumberFormat
import java.util.Locale

class MyListingAdapter(
    private val onItemClick: (ListingEntity) -> Unit,
    private val onMarkSoldClick: (ListingEntity) -> Unit,
    private val onDeleteClick: (ListingEntity) -> Unit
) : ListAdapter<ListingEntity, MyListingAdapter.MyListingViewHolder>(ListingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_my_listing, parent, false)
        return MyListingViewHolder(view, onItemClick, onMarkSoldClick, onDeleteClick)
    }

    override fun onBindViewHolder(holder: MyListingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyListingViewHolder(
        itemView: View,
        private val onItemClick: (ListingEntity) -> Unit,
        private val onMarkSoldClick: (ListingEntity) -> Unit,
        private val onDeleteClick: (ListingEntity) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        
        private val ivImage: ImageView = itemView.findViewById(R.id.ivListingImage)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvCondition: TextView = itemView.findViewById(R.id.tvCondition)
        private val tvSoldBadge: TextView = itemView.findViewById(R.id.tvSoldBadge)
        private val btnMarkSold: Button = itemView.findViewById(R.id.btnMarkSold)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)

        fun bind(listing: ListingEntity) {
            tvTitle.text = listing.title
            val formattedPrice = NumberFormat.getNumberInstance(Locale("en", "IN")).format(listing.price)
            tvPrice.text = "Rs. $formattedPrice"
            tvCategory.text = listing.category
            tvCondition.text = listing.condition

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
                btnMarkSold.text = "Mark Available"
            } else {
                tvSoldBadge.visibility = View.GONE
                btnMarkSold.text = "Mark Sold"
            }

            itemView.setOnClickListener {
                onItemClick(listing)
            }
            
            btnMarkSold.setOnClickListener {
                onMarkSoldClick(listing)
            }
            
            btnDelete.setOnClickListener {
                onDeleteClick(listing)
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