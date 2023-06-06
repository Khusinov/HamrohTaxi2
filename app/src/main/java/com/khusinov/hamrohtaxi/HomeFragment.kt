package com.khusinov.hamrohtaxi

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.khusinov.hamrohtaxi.databinding.FragmentHomeBinding
import com.khusinov.hamrohtaxi.models.MyPosts
import com.khusinov.hamrohtaxi.models.Post
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding { FragmentHomeBinding.bind(it) }
    private val TAG = "HomeFragment"
    var postsByDrivers = ArrayList<Post>()
    var postsByPassengers = ArrayList<Post>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            searchbar.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
            }

            drivers.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    callAdapter(postsByDrivers)
            }

            passangers.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked)
                    callAdapter(postsByPassengers)
            }

            val postList = ArrayList<Post>()
            Common.retrofitServices.getAllPosts().enqueue(object : Callback<MyPosts> {
                override fun onResponse(call: Call<MyPosts>, response: Response<MyPosts>) {

                    Log.d(TAG, "onResponse: posts ${response.isSuccessful}")
                    Log.d(TAG, "onResponse: posts ${response.body()}")

                    if (response.isSuccessful) {

                        postsByDrivers.clear()
                        postsByPassengers.clear()

                        val list = response.body()?.results
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
                                val price = i.price

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
                                    role,
                                    price
                                )
                                if (role == 0)
                                    postsByPassengers.add(post)
                                if (role == 1)
                                    postsByDrivers.add(post)
                            }
                            if (drivers.isChecked) {
                                callAdapter(postsByDrivers)
                            }
                            if (passangers.isChecked) {
                                callAdapter(postsByPassengers)
                            }
                        }

                    }

                }

                override fun onFailure(call: Call<MyPosts>, t: Throwable) {

                    Log.d(TAG, "onFailure: ${t.message}")
                    Log.d(TAG, "onFailure: ${t.cause}")
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


                //   findNavController().navigate(R.id.action_homeFragment_to_phoneNumberFragment)
                val dialIntent = Intent(Intent.ACTION_DIAL)
                dialIntent.data = Uri.parse("tel:" + it.user.phone_number)
                startActivity(dialIntent)

            }
        }
    }
}