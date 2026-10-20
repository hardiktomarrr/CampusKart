package com.example.campuskart.ui.welcome

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.campuskart.R

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val sharedPrefs = requireActivity().getSharedPreferences("CampusKartPrefs", Context.MODE_PRIVATE)
        val hasSeenWelcome = sharedPrefs.getBoolean("hasSeenWelcome", false)

        if (hasSeenWelcome) {
            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
            return
        }

        view.findViewById<Button>(R.id.btnContinue).setOnClickListener {
            sharedPrefs.edit().putBoolean("hasSeenWelcome", true).apply()
            findNavController().navigate(R.id.action_welcomeFragment_to_homeFragment)
        }
    }
}
