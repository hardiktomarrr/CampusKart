package com.example.campuskart.ui.mylistings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.campuskart.CampusKartApplication
import com.example.campuskart.R
import com.example.campuskart.viewmodel.ListingViewModel
import com.example.campuskart.viewmodel.ListingViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MyListingsFragment : Fragment() {

    private val viewModel: ListingViewModel by activityViewModels {
        ListingViewModelFactory((requireActivity().application as CampusKartApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_listings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvMyListings = view.findViewById<RecyclerView>(R.id.rvMyListings)
        val tvEmptyState = view.findViewById<TextView>(R.id.tvEmptyState)

        val adapter = MyListingAdapter(
            onItemClick = { listing ->
                val bundle = Bundle().apply {
                    putLong("listingId", listing.id)
                }
                findNavController().navigate(R.id.action_myListingsFragment_to_detailsFragment, bundle)
            },
            onMarkSoldClick = { listing ->
                viewModel.updateListingStatus(listing.id, !listing.isSold)
            },
            onDeleteClick = { listing ->
                AlertDialog.Builder(requireContext())
                    .setTitle("Delete Listing")
                    .setMessage("Are you sure you want to delete this listing?")
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteListing(listing)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }
        )
        rvMyListings.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myListings.collectLatest { listings ->
                    adapter.submitList(listings)
                    if (listings.isEmpty()) {
                        tvEmptyState.visibility = View.VISIBLE
                        rvMyListings.visibility = View.GONE
                    } else {
                        tvEmptyState.visibility = View.GONE
                        rvMyListings.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
