package com.example.campuskart

import android.app.Application
import com.example.campuskart.data.AppDatabase
import com.example.campuskart.data.ListingRepository
import com.example.campuskart.data.SampleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CampusKartApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { ListingRepository(database.listingDao()) }

    override fun onCreate() {
        super.onCreate()
        
        // Prepopulate / sync sample data in database
        applicationScope.launch {
            val initialListings = SampleData.getInitialListings()
            initialListings.forEach { listing ->
                repository.insertListing(listing)
            }
        }
    }
}