package com.example.campuskart.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "favourites",
    foreignKeys = [
        ForeignKey(
            entity = ListingEntity::class,
            parentColumns = ["id"],
            childColumns = ["listingId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class  FavouriteEntity(
    @PrimaryKey val listingId: Long,
    val savedAt: Long = System.currentTimeMillis()
)
