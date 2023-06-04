package com.khusinov.hamrohtaxi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.khusinov.hamrohtaxi.adapter.MyPostAdapter
import com.khusinov.hamrohtaxi.databinding.FragmentChatBinding
import com.khusinov.hamrohtaxi.models.DeleteResponse
import com.khusinov.hamrohtaxi.models.MyPosts
import com.khusinov.hamrohtaxi.models.Post
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding by viewBinding { FragmentChatBinding.bind(it) }
    private val TAG = "ChatFragment"
    private var sharedPreferences: SharedPreferences? = null
    var tokenn = ""

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

            sharedPreferences = requireContext().getSharedPreferences("HamrohTaxi", 0)
            var token = sharedPreferences?.getString("access", "").toString()

            token = "Bearer $token"
            tokenn = token

            Log.d(TAG, "setupUI: token ${token.toString()}")

            Common.retrofitServices.getMyPosts(token).enqueue(object : Callback<MyPosts> {
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

            val adapter = MyPostAdapter()
            val recyclerView = rv

            recyclerView.adapter = adapter
            adapter.submitList(postList!!)

            adapter.onClick = {
                // DELETE

                Log.d(TAG, "callAdapter: post id ${it.id}")
                Log.d(TAG, "callAdapter: $tokenn")

                Common.retrofitServices.deletePostById(it.id, tokenn)
                    .enqueue(object : Callback<DeleteResponse> {
                        override fun onResponse(
                            call: Call<DeleteResponse>,
                            response: Response<DeleteResponse>
                        ) {

                            Log.d(TAG, "onResponse: ${response.body()?.status.toString()}")
                            if (response.body()?.status.toString() == "deleted") {
                                Toast.makeText(requireContext(), "O'chirildi", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {

                            Log.d(TAG, "onFailure: ${t.message}")
                            Toast.makeText(requireContext(), "Failed.", Toast.LENGTH_SHORT).show()
                        }


                    })

                adapter.notifyItemRemoved(postList.size)
            }
            adapter.onClick2 = {
                // EDIT
                Log.d(TAG, "callAdapter: ${it.id}")

                val sharedPref = context?.getSharedPreferences("HamrohTaxi", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putString("id", "${it.id}")
                editor?.putString("from_location", "${it.from_location}")
                editor?.putString("to_location", "${it.to_location}")
                editor?.putString("go_time", "${it.go_time}")
                editor?.putString("price", "${it.price}")
                editor?.putString("addition", "${it.addition}")
                editor?.putString("count", "${it.count}")
                editor?.putString("user_role", "${it.user_role}")
                editor?.apply()


                findNavController().navigate(R.id.action_chatFragment_to_updatePostFragment)


            }

        }

    }

}