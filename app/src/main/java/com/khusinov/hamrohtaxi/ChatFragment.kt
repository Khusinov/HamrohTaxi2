package com.khusinov.hamrohtaxi

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.khusinov.hamrohtaxi.databinding.FragmentChatBinding


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding by viewBinding { FragmentChatBinding.bind(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {

        binding.apply {

            createNewPost.setOnClickListener {

                findNavController().navigate(R.id.action_chatFragment_to_newPostFragment)
            }


        }


    }

}