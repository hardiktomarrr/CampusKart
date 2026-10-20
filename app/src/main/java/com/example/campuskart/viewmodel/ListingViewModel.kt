package com.example.campuskart.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskart.data.ListingEntity
import com.example.campuskart.data.ListingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

class ListingViewModel(private val repository: ListingRepository) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val categoryFilter = MutableStateFlow("All")

    val allListings = repository.allListings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favouriteListings = repository.favouriteListings.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val favouriteListingIds = repository.favouriteListingIds.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val myListings = repository.getUserListings("demo_user").stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val availableListings = combine(
        repository.availableListings,
        searchQuery,
        categoryFilter
    ) { listings, query, category ->
        listings.filter { listing ->
            val matchesQuery = listing.title.contains(query, ignoreCase = true) ||
                    listing.description.contains(query, ignoreCase = true)
            val matchesCategory = category == "All" || listing.category == category
            matchesQuery && matchesCategory
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun setCategoryFilter(category: String) {
        categoryFilter.value = category
    }

    fun toggleFavourite(listingId: Long) {
        viewModelScope.launch {
            val isFav = favouriteListingIds.value.contains(listingId)
            repository.toggleFavourite(listingId, !isFav)
        }
    }

    fun addListing(listing: ListingEntity) {
        viewModelScope.launch {
            repository.insertListing(listing)
        }
    }

    fun markSold(listing: ListingEntity) {
        viewModelScope.launch {
            repository.updateListing(listing.copy(isSold = true))
        }
    }

    fun deleteListing(listing: ListingEntity) {
        viewModelScope.launch {
            repository.deleteListing(listing)
        }
    }

    fun getListing(id: Long) = repository.getListingById(id)

    fun updateListingStatus(listingId: Long, isSold: Boolean) {
        viewModelScope.launch {
            val listing = repository.getListingById(listingId).first()
            repository.updateListing(listing.copy(isSold = isSold))
        }
    }
}
