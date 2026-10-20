package com.example.campuskart.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.campuskart.CampusKartApplication
import com.example.campuskart.R
import com.example.campuskart.ui.home.ListingAdapter
import com.example.campuskart.viewmodel.ListingViewModel
import com.example.campuskart.viewmodel.ListingViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    private val viewModel: ListingViewModel by activityViewModels {
        ListingViewModelFactory((requireActivity().application as CampusKartApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvFavourites = view.findViewById<RecyclerView>(R.id.rvFavourites)
        val tvEmptyState = view.findViewById<TextView>(R.id.tvEmptyState)

        val adapter = ListingAdapter { listing ->
            val bundle = Bundle().apply {
                putLong("listingId", listing.id)
            }
            findNavController().navigate(R.id.action_favouritesFragment_to_detailsFragment, bundle)
        }
        rvFavourites.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.favouriteListings.collectLatest { listings ->
                    adapter.submitList(listings)
                    if (listings.isEmpty()) {
                        tvEmptyState.visibility = View.VISIBLE
                        rvFavourites.visibility = View.GONE
                    } else {
                        tvEmptyState.visibility = View.GONE
                        rvFavourites.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
