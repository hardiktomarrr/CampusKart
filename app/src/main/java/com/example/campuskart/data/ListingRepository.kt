package com.example.campuskart.data

import kotlinx.coroutines.flow.Flow

class ListingRepository(private val listingDao: ListingDao) {
    val allListings: Flow<List<ListingEntity>> = listingDao.getAllListings()
    val availableListings: Flow<List<ListingEntity>> = listingDao.getAvailableListings()
    val favouriteListings: Flow<List<ListingEntity>> = listingDao.getFavouriteListings()
    val favouriteListingIds: Flow<List<Long>> = listingDao.getFavouriteListingIds()

    fun getListingById(id: Long): Flow<ListingEntity> {
        return listingDao.getListingById(id)
    }

    fun getUserListings(ownerId: String = "demo_user"): Flow<List<ListingEntity>> {
        return listingDao.getUserListings(ownerId)
    }

    suspend fun insertListing(listing: ListingEntity): Long {
        return listingDao.insertListing(listing)
    }

    suspend fun updateListing(listing: ListingEntity) {
        listingDao.updateListing(listing)
    }

    suspend fun deleteListing(listing: ListingEntity) {
        listingDao.deleteListing(listing)
    }

    suspend fun toggleFavourite(listingId: Long, isFavourite: Boolean) {
        if (isFavourite) {
            listingDao.insertFavourite(FavouriteEntity(listingId = listingId))
        } else {
            listingDao.deleteFavourite(listingId)
        }
    }

    suspend fun getListingCount(): Int {
        return listingDao.getListingCount()
    }
}
