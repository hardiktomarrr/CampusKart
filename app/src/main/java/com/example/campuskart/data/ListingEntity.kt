package com.example.campuskart.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "listings")
data class ListingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val price: Double,
    val category: String,
    val condition: String,
    val imageResName: String,
    val sellerName: String,
    val sellerPhone: String,
    val ownerId: String,
    val isSold: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
