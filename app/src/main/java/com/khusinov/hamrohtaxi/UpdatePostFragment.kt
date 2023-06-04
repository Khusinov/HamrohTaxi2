package com.khusinov.hamrohtaxi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khusinov.hamrohtaxi.databinding.FragmentUpdatePostBinding

class UpdatePostFragment : Fragment(R.layout.fragment_update_post) {
    private val binding by viewBinding { FragmentUpdatePostBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {


        }

    }

}