package com.example.campuskart.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
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
import com.example.campuskart.viewmodel.ListingViewModel
import com.example.campuskart.viewmodel.ListingViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val viewModel: ListingViewModel by activityViewModels {
        ListingViewModelFactory(
            (requireActivity().application as CampusKartApplication).repository
        )
    }

    private lateinit var adapter: ListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvListings = view.findViewById<RecyclerView>(R.id.rvListings)
        val tvEmptyState = view.findViewById<TextView>(R.id.tvEmptyState)
        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val spinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategory)
        val fabAddListing = view.findViewById<FloatingActionButton>(R.id.fabAddListing)

        adapter = ListingAdapter { listing ->
            val bundle = Bundle().apply {
                putLong("listingId", listing.id)
            }
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }
        rvListings.adapter = adapter

        // Setup Spinner
        val categories = arrayOf("All categories", "Books & Notes", "Electronics", "Appliances", "Sports", "Other")
        val spinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerCategory.adapter = spinnerAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selected = categories[position]
                viewModel.setCategoryFilter(if (selected == "All categories") "All" else selected)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Setup Search
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s.toString())
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Setup FAB
        fabAddListing.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addListingFragment)
        }

        // Observe Data
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.availableListings.collect { listings ->
                    adapter.submitList(listings)
                    if (listings.isEmpty()) {
                        tvEmptyState.visibility = View.VISIBLE
                        rvListings.visibility = View.GONE
                    } else {
                        tvEmptyState.visibility = View.GONE
                        rvListings.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
