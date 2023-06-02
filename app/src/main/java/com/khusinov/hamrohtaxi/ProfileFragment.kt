package com.khusinov.hamrohtaxi

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khusinov.hamrohtaxi.databinding.FragmentProfileBinding


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding by viewBinding { FragmentProfileBinding.bind(it) }
    private var sharedPreferences: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            sharedPreferences = requireContext().getSharedPreferences("HamrohTaxi", 0)
            var name = sharedPreferences?.getString("name", "").toString()
            var phoneNumber = sharedPreferences?.getString("phoneNumber", "").toString()

            username.text = name
            phoneNumbertv.text = phoneNumber



        }
    }

}