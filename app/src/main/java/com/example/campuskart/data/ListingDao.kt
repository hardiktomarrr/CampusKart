package com.example.campuskart.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ListingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListing(listing: ListingEntity): Long

    @Update
    suspend fun updateListing(listing: ListingEntity)

    @Delete
    suspend fun deleteListing(listing: ListingEntity)

    @Query("SELECT * FROM listings ORDER BY createdAt DESC")
    fun getAllListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE isSold = 0 ORDER BY createdAt DESC")
    fun getAvailableListings(): Flow<List<ListingEntity>>

    @Query("SELECT * FROM listings WHERE id = :id")
    fun getListingById(id: Long): Flow<ListingEntity>

    @Query("SELECT * FROM listings WHERE ownerId = :ownerId ORDER BY createdAt DESC")
    fun getUserListings(ownerId: String = "demo_user"): Flow<List<ListingEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favourite: FavouriteEntity)

    @Query("DELETE FROM favourites WHERE listingId = :listingId")
    suspend fun deleteFavourite(listingId: Long)

    @Query("SELECT listingId FROM favourites")
    fun getFavouriteListingIds(): Flow<List<Long>>

    @Query("SELECT l.* FROM listings l INNER JOIN favourites f ON l.id = f.listingId ORDER BY f.savedAt DESC")
    fun getFavouriteListings(): Flow<List<ListingEntity>>
    
    @Query("SELECT COUNT(*) FROM listings")
    suspend fun getListingCount(): Int
}
