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
import android.widget.*
import com.khusinov.hamrohtaxi.databinding.FragmentNewPostBinding
import com.khusinov.hamrohtaxi.models.*
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewPostFragment : Fragment(R.layout.fragment_new_post), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val binding by viewBinding { FragmentNewPostBinding.bind(it) }
    private val TAG = "NewPostFragment"
    private var sharedPreferences: SharedPreferences? = null
    private var districts = ArrayList<District>()
    private val regionNameList = ArrayList<String>()
    private val districtNameList = ArrayList<String>()
    private val districtIds = ArrayList<Int>()
    private val regionIds = ArrayList<Int>()
    private var selectedRegionFrom = ""
    private var selectedRegionTo = ""
    private var selectedDistrictFrom = ""
    private var selectedDistrictTo = ""
    private var regionId = 0
    private var districtId = 0
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
            var token = sharedPreferences?.getString("access", "").toString()

            token = "Bearer $token"

            Log.d(TAG, "setupUI: token ${token.toString()}")

            date.setOnClickListener {

                getDateTimeCalendar()
                DatePickerDialog(requireContext(), this@NewPostFragment, year, month, day).show()

            }

            time.setOnClickListener {
                getDateTimeCalendar()
                TimePickerDialog(requireContext(), this@NewPostFragment, hour, minute, true).show()
            }

            postBtn.setOnClickListener {

                var userRole = if (driver.isChecked) 1 else 0
                var fromLocation = selectedDistrictFrom
                var toLocation = selectedDistrictTo
                // 2023-6-24T05:09:54.995Z  time format
                var goTime = "$savedYear-$savedMonth-${savedDay} $savedHour:${savedMinute}"
                var count = numberOfPeople.text.toString().toInt()
                var price = price.text.toString()
                var addition = comment.text.toString()

                var post2 =
                    Post2(addition, count, fromLocation, goTime, price, toLocation, userRole)

                Log.d(TAG, "setupUI: ${post2.toString()}")
                Log.d(TAG, "setupUI: $token")

                Common.retrofitServices.createPost(post2, token).enqueue(object : Callback<Post3> {
                    override fun onResponse(call: Call<Post3>, response: Response<Post3>) {

                        Log.d(TAG, "onResponse: $response")
                        Log.d(TAG, "onResponse: ${response.body()}")
                        Log.d(TAG, "onResponse: ${response.message()}")

                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Post yaratildi", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<Post3>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                        Toast.makeText(requireContext(), "Internetni tekshiring", Toast.LENGTH_SHORT).show()
                    }

                })
            }


            Common.retrofitServices.getRegions().enqueue(object : Callback<List<Region>> {
                override fun onResponse(
                    call: Call<List<Region>>,
                    response: Response<List<Region>>
                ) {
                    Log.d(TAG, "onResponse: $response")
                    if (response.isSuccessful) {
                        val regionList = response.body()
                        for (i in regionList!!) {
                            regionNameList.add(i.region_uz)
                            regionIds.add(i.id)

                        }
                        setupRegionSpinner(regionNameList, regionIds)
                        setupRegionSpinner2(regionNameList, regionIds)

                    }
                }

                override fun onFailure(call: Call<List<Region>>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })

            Common.retrofitServices.getDistricts().enqueue(object : Callback<List<District>> {
                override fun onResponse(
                    call: Call<List<District>>,
                    response: Response<List<District>>
                ) {
                    Log.d(TAG, "onResponse2: $response")
                    if (response.isSuccessful) {

                        districts = response.body() as ArrayList<District>
                    }

                }

                override fun onFailure(call: Call<List<District>>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.message}")
                }
            })


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

    private fun setupRegionSpinner(
        nameList: List<String>,
        regionIDs: ArrayList<Int>,
    ) {

        val spinner = binding.locationFromRegion
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, nameList)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                regionId = regionIDs[position]
                selectedRegionFrom = nameList[position]

                callDistrictByID(regionId)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun setupRegionSpinner2(
        nameList: List<String>,
        regionIDs: ArrayList<Int>,
    ) {

        val spinner = binding.locationToRegion
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, nameList)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                regionId = regionIDs[position]
                selectedRegionTo = nameList[position]

                callDistrictByID2(regionId)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun callDistrictByID(regionId: Int) {

        var districtsList = ArrayList<District>()

        districtsList.clear()
        districtNameList.clear()
        districtIds.clear()

        for (i in districts) {
            if (i.region_id == regionId) {
                districtsList.add(i)
                districtNameList.add(i.district_uz)
                districtIds.add(i.id)
            }
            setupDistrictSpinner(districtNameList, districtIds)
        }

    }

    private fun callDistrictByID2(regionId: Int) {

        var districtsList = ArrayList<District>()

        districtsList.clear()
        districtNameList.clear()
        districtIds.clear()

        for (i in districts) {
            if (i.region_id == regionId) {
                districtsList.add(i)
                districtNameList.add(i.district_uz)
                districtIds.add(i.id)
            }
            setupDistrictSpinner2(districtNameList, districtIds)
        }

    }

    private fun setupDistrictSpinner(nameList: List<String>, districtsIDs: ArrayList<Int>) {

        val spinner = binding.locationFromDistrict
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, nameList)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                districtId = districtsIDs[position]
                selectedDistrictFrom = nameList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
    }

    private fun setupDistrictSpinner2(nameList: List<String>, districtsIDs: ArrayList<Int>) {

        val spinner = binding.locationToDistrict
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.spinner_item, nameList)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                districtId = districtsIDs[position]
                selectedDistrictTo = nameList[position]

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
            }
        }
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