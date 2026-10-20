package com.example.campuskart.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.campuskart.CampusKartApplication
import com.example.campuskart.R
import com.example.campuskart.viewmodel.ListingViewModel
import com.example.campuskart.viewmodel.ListingViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class DetailsFragment : Fragment() {

    private val viewModel: ListingViewModel by activityViewModels {
        ListingViewModelFactory((requireActivity().application as CampusKartApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listingId = arguments?.getLong("listingId") ?: return

        val ivBack = view.findViewById<ImageView>(R.id.ivBack)
        val ivImage = view.findViewById<ImageView>(R.id.ivListingImage)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val tvPrice = view.findViewById<TextView>(R.id.tvPrice)
        val tvCategory = view.findViewById<TextView>(R.id.tvCategory)
        val tvCondition = view.findViewById<TextView>(R.id.tvCondition)
        val tvSellerInfo = view.findViewById<TextView>(R.id.tvSellerInfo)
        val tvDescription = view.findViewById<TextView>(R.id.tvDescription)
        val btnContactSeller = view.findViewById<View>(R.id.btnContactSeller)
        val btnFavourite = view.findViewById<ImageButton>(R.id.btnFavourite)

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.getListing(listingId).collectLatest { listing ->
                        if (listing != null) {
                            tvTitle.text = listing.title
                            val formattedPrice = NumberFormat.getNumberInstance(Locale("en", "IN")).format(listing.price)
                            tvPrice.text = "Rs. $formattedPrice"
                            tvCategory.text = "Category: ${listing.category}"
                            tvCondition.text = "Condition: ${listing.condition}"
                            tvSellerInfo.text = "Seller: ${listing.sellerName}"
                            tvDescription.text = listing.description

                            // Load Image
                            val context = requireContext()
                            val imageResId = context.resources.getIdentifier(
                                listing.imageResName,
                                "drawable",
                                context.packageName
                            )

                            if (imageResId != 0) {
                                ivImage.setImageResource(imageResId)
                            } else {
                                ivImage.setImageResource(android.R.color.transparent)
                                ivImage.setBackgroundColor(android.graphics.Color.parseColor("#E8EAF6"))
                            }
                            
                            btnContactSeller.setOnClickListener {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:${listing.sellerPhone}")
                                }
                                try {
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(requireContext(), "No dialer app found", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                
                launch {
                    viewModel.favouriteListingIds.collectLatest { favIds ->
                        val isFav = favIds.contains(listingId)
                        if (isFav) {
                            btnFavourite.setImageResource(android.R.drawable.btn_star_big_on)
                        } else {
                            btnFavourite.setImageResource(android.R.drawable.btn_star_big_off)
                        }
                    }
                }
            }
        }
        
        btnFavourite.setOnClickListener {
            viewModel.toggleFavourite(listingId)
        }
    }
}