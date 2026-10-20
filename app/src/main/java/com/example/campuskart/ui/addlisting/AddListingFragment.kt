package com.example.campuskart.ui.addlisting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import android.widget.ImageView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.campuskart.CampusKartApplication
import com.example.campuskart.R
import com.example.campuskart.data.ListingEntity
import com.example.campuskart.viewmodel.ListingViewModel
import com.example.campuskart.viewmodel.ListingViewModelFactory
import com.google.android.material.snackbar.Snackbar

class AddListingFragment : Fragment() {

    private val viewModel: ListingViewModel by activityViewModels {
        ListingViewModelFactory(
            (requireActivity().application as CampusKartApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        val etPrice = view.findViewById<EditText>(R.id.etPrice)
        val spinnerCategory = view.findViewById<Spinner>(R.id.spinnerCategory)
        val spinnerCondition = view.findViewById<Spinner>(R.id.spinnerCondition)
        val btnSubmit = view.findViewById<LinearLayout>(R.id.btnSubmit)
        val ivBack = view.findViewById<ImageView>(R.id.ivBack)

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Setup Spinners
        val categories = arrayOf("Books & Notes", "Electronics", "Furniture", "Clothing", "Other")
        val categoryAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, categories)
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerCategory.adapter = categoryAdapter

        val conditions = arrayOf("Like New", "Good", "Used")
        val conditionAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, conditions)
        conditionAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinnerCondition.adapter = conditionAdapter

        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val priceStr = etPrice.text.toString().trim()
            val category = spinnerCategory.selectedItem.toString()
            val condition = spinnerCondition.selectedItem.toString()

            if (title.isEmpty()) {
                etTitle.error = "Title cannot be empty"
                return@setOnClickListener
            }
            if (priceStr.isEmpty()) {
                etPrice.error = "Price cannot be empty"
                return@setOnClickListener
            }

            val price = priceStr.toDoubleOrNull()
            if (price == null || price <= 0) {
                etPrice.error = "Enter a valid price"
                return@setOnClickListener
            }

            val listing = ListingEntity(
                title = title,
                description = description,
                price = price,
                category = category,
                condition = condition,
                imageResName = "placeholder",
                sellerName = "Demo User",
                sellerPhone = "1234567890",
                ownerId = "demo_user"
            )

            viewModel.addListing(listing)
            Snackbar.make(view, "Listing Added Successfully!", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }
}
