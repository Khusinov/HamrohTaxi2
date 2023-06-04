package com.khusinov.hamrohtaxi

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khusinov.hamrohtaxi.databinding.FragmentUpdatePostBinding

class UpdatePostFragment : Fragment(R.layout.fragment_update_post) {
    private val binding by viewBinding { FragmentUpdatePostBinding.bind(it) }
    private val TAG = "UpdatePostFragment"
    private var sharedPreferences: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            sharedPreferences = requireContext().getSharedPreferences("HamrohTaxi", 0)
            var id = sharedPreferences?.getString("id", "").toString()
            var fromLocation = sharedPreferences?.getString("from_location", "").toString()
            var toLocation = sharedPreferences?.getString("to_location", "").toString()
            var goTime1 = sharedPreferences?.getString("go_time", "").toString()
            var price1 = sharedPreferences?.getString("price", "").toString()
            var addition1 = sharedPreferences?.getString("addition", "").toString()
            var count1 = sharedPreferences?.getString("count", "").toString()
            var userRole = sharedPreferences?.getString("user_role", "0")?.toInt()

            if (userRole == 1) {
                driver.isChecked = true
                passanger.isChecked = false
            } else {
                passanger.isChecked = true
                driver.isChecked = false
            }

            selectedLocationFrom.text = fromLocation
            selectedLocationTo.text = toLocation
            price.setText(price1)
            comment.setText(addition1)
            numberOfPeople.setText(count1)

            var slashId = goTime1.indexOf(" ")
            var selectedDate = goTime1.subSequence(0, slashId)
            var selectedTime = goTime1.subSequence(slashId, goTime1.length)

            dateTV.text = selectedDate
            timeTv.text = selectedTime
            Log.d(TAG, "setupUI: date  $selectedDate")
            Log.d(TAG, "setupUI: time  $selectedTime")


        }

    }

}