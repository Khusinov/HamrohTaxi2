package com.khusinov.hamrohtaxi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.khusinov.hamrohtaxi.databinding.FragmentChatBinding
import com.khusinov.hamrohtaxi.models.MyPosts
import com.khusinov.hamrohtaxi.models.Post
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding by viewBinding { FragmentChatBinding.bind(it) }
    private val TAG = "ChatFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {

        binding.apply {

            createNewPost.setOnClickListener {

                findNavController().navigate(R.id.action_chatFragment_to_newPostFragment)
            }

            val list = ArrayList<Post>()

            Common.retrofitServices.getMyPosts("").enqueue(object : Callback<MyPosts> {
                override fun onResponse(call: Call<MyPosts>, response: Response<MyPosts>) {
                    Log.d(TAG, "onResponse: $response")
                    if (response.isSuccessful && response.body()?.count != 0) {

                        val postList = response.body()?.results
                        callAdapter(postList)

                    }

                }

                override fun onFailure(call: Call<MyPosts>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }

            })


        }


    }

    private fun callAdapter(postList: List<Post>?) {

        binding.apply {

            noPostTv.visibility = View.GONE
            rv.visibility = View.VISIBLE

            val adapter = PostAdapter()
            val recyclerView = rv

            recyclerView.adapter = adapter
            adapter.submitList(postList!!)

        }

    }

}