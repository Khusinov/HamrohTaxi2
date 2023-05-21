package com.khusinov.hamrohtaxi

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.khusinov.hamrohtaxi.databinding.ItemPostBinding
import com.khusinov.hamrohtaxi.models.Post


class PostAdapter() : RecyclerView.Adapter<PostAdapter.PartOneViewHolder>() {

    private val dif = AsyncListDiffer(this, ITEM_DIFF)

       var onClick: ((Post) -> Unit)? = null

    private val TAG = "PostAdapter"

    inner class PartOneViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {

            val postCurrent = dif.currentList[adapterPosition]

            binding.apply {

                val locationFrom1 = postCurrent.from_location
                val index = locationFrom1.indexOf("-")

                val locationFrom2 = locationFrom1.substring(index + 1)
                Log.d(TAG, "bind: location $locationFrom2")

                val locationTo1 = postCurrent.to_location
                val index2 = locationTo1.indexOf("-")
                val locationTo2 = locationTo1.substring(index2+1)
                Log.d(TAG, "bind: location $locationTo2")


                name.text = postCurrent.user.name
                locationFrom.text = locationFrom2
                locationTo.text =locationTo2
                time.text = postCurrent.go_time
                postedTime.text = postCurrent.posted_time
                personCount.text = postCurrent.count.toString()

                btnBePartner.setOnClickListener {
                    onClick?.invoke(postCurrent)
                }


                binding.root.setOnClickListener {

                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartOneViewHolder =
        PartOneViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = dif.currentList.size

    override fun onBindViewHolder(holder: PartOneViewHolder, position: Int) = holder.bind()

    fun submitList(list: List<Post>) {
        dif.submitList(list)
    }

    companion object {
        private val ITEM_DIFF = object : DiffUtil.ItemCallback<Post>() {
            override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem.user == newItem.user

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean =
                oldItem == newItem
        }
    }

}