package com.khusinov.hamrohtaxi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.khusinov.hamrohtaxi.databinding.FragmentHomeBinding
import com.khusinov.hamrohtaxi.models.Post
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val TAG = "HomeFragment"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            val postList = ArrayList<Post>()
            Common.retrofitServices.getAllPosts().enqueue(object : Callback<List<Post>> {
                override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {

                    Log.d(TAG, "onResponse: posts ${response.isSuccessful}")
                    Log.d(TAG, "onResponse: posts ${response.body()}")

                    if (response.isSuccessful) {

                        val list = response.body()
                        if (list != null) {
                            for (i in list) {
                                val user = i.user
                                val from = i.from_location
                                val to = i.to_location
                                val goTime = i.go_time
                                val postedTime = i.posted_time
                                val postId = i.id
                                val addition = i.addition
                                val count = i.count
                                val role = i.user_role
                                val status = i.status

                                val post = Post(
                                    addition,
                                    count,
                                    from,
                                    goTime,
                                    postId,
                                    postedTime,
                                    status,
                                    to,
                                    user,
                                    role
                                )

                                postList.add(post)
                            }
                            callAdapter(postList)
                        }


                    }

                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {

                    Log.d(TAG, "onFailure: ${t.message}")
                }

            })


        }

    }

    private fun callAdapter(postList: java.util.ArrayList<Post>) {

        binding.apply {


            val adapter = PostAdapter()

            var recyclerView = rv
            rv.adapter = adapter
            adapter.submitList(postList)

            adapter.onClick = {

                findNavController().navigate(R.id.action_homeFragment_to_phoneNumberFragment)

            }
        }
    }
}