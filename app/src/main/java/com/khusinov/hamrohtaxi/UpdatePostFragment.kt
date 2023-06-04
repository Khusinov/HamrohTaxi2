package com.khusinov.hamrohtaxi

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.khusinov.hamrohtaxi.databinding.FragmentUpdatePostBinding
import com.khusinov.hamrohtaxi.models.Post2
import com.khusinov.hamrohtaxi.models.Post3
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.math.log

class UpdatePostFragment : Fragment(R.layout.fragment_update_post) , DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val binding by viewBinding { FragmentUpdatePostBinding.bind(it) }
    private val TAG = "UpdatePostFragment"
    private var sharedPreferences: SharedPreferences? = null
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            sharedPreferences = requireContext().getSharedPreferences("HamrohTaxi", 0)
            var id = sharedPreferences?.getString("id", "0").toString().toInt()
            var fromLocation = sharedPreferences?.getString("from_location", "").toString()
            var toLocation = sharedPreferences?.getString("to_location", "").toString()
            var goTime1 = sharedPreferences?.getString("go_time", "").toString()
            var price1 = sharedPreferences?.getString("price", "").toString()
            var addition1 = sharedPreferences?.getString("addition", "").toString()
            var count1 = sharedPreferences?.getString("count", "").toString()
            var userRole = sharedPreferences?.getString("user_role", "0")?.toInt()

            var token = sharedPreferences?.getString("access", "").toString()
            token = "Bearer $token"

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

            date.setOnClickListener {
                getDateTimeCalendar()
                DatePickerDialog(requireContext(), this@UpdatePostFragment, year, month, day).show()

            }

            time.setOnClickListener {
                getDateTimeCalendar()
                TimePickerDialog(requireContext(), this@UpdatePostFragment, hour, minute, true).show()
            }

            saveBtn.setOnClickListener {

                val userRole1 = if (driver.isChecked) 1 else 0
                val fromLocation1 = selectedLocationFrom.text.toString()
                val toLocation1 = selectedLocationTo.text.toString()
                // 2023-6-24T05:09:54.995Z  time format
                val goTime = "$savedYear-$savedMonth-${savedDay} $savedHour:${savedMinute}"
                val count = numberOfPeople.text.toString().toInt()
                val price = price.text.toString()
                val addition = comment.text.toString()

                val post2 =
                    Post2(addition, count, fromLocation1, goTime, price, toLocation1, userRole1)



                Log.d(TAG, "setupUI: ${post2.toString()}")
                Log.d(TAG, "setupUI: $token")

                Common.retrofitServices.updatePostById(id , post2 , token).enqueue(object : Callback<Post3>{
                    override fun onResponse(call: Call<Post3>, response: Response<Post3>) {
                        if (response.isSuccessful) {
                            Log.d(TAG, "onResponse: did it")
                            Toast.makeText(requireContext(), "Saqlandi", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Sana va vahtni qayta tanlang!", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "onResponse: ${response.message()}")
                        }
                        
                    }

                    override fun onFailure(call: Call<Post3>, t: Throwable) {
                        Toast.makeText(requireContext(), "Xato!", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onFailure: ${t.message}")
                    }

                })

            }

            
        }

    }
    private fun getDateTimeCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        binding.dateTV.text = "$savedDay.$savedMonth.$savedYear"
    }

   override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute

        binding.timeTv.text = "$savedHour:$savedMinute"
    }

}