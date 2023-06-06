package com.khusinov.hamrohtaxi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.khusinov.hamrohtaxi.databinding.FragmentSearchBinding
import com.khusinov.hamrohtaxi.models.Post
import com.khusinov.hamrohtaxi.models.Post3
import com.khusinov.hamrohtaxi.models.SearchResponse
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding { FragmentSearchBinding.bind(it) }
    private val TAG = "SearchFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            searchView.setOnClickListener {

                var query = searchView.query.toString()

                query.trim()

                var postList1 = ArrayList<Post>()

                Common.retrofitServices.search(query).enqueue(object : Callback<SearchResponse> {
                    override fun onResponse(
                        call: Call<SearchResponse>,
                        response: Response<SearchResponse>
                    ) {
                        if (response.isSuccessful) {

                            var postList = response.body()!!.results
                            for (i in postList) {
                                postList1.add(i)
                            }
                            if (postList1.isNotEmpty())
                                callAdapter(postList1)
                            else {
                                Toast.makeText(
                                    requireContext(),
                                    "Malumot topilmadi",
                                    Toast.LENGTH_SHORT
                                ).show()
                                callAdapter(postList1)
                            }

                        }

                    }

                    override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                        Toast.makeText(requireContext(), "Ma'lumot topilmadi", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }

        }
    }

    private fun callAdapter(postList1: java.util.ArrayList<Post>) {

        binding.apply {

            val adapter = PostAdapter()
            val rv = recyclerView

            rv.adapter = adapter
            adapter.submitList(postList1)


        }
    }
}