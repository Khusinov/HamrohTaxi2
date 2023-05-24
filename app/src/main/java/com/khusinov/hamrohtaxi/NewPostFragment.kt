package com.khusinov.hamrohtaxi

import android.app.DatePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.khusinov.hamrohtaxi.databinding.FragmentNewPostBinding
import com.khusinov.hamrohtaxi.models.District
import com.khusinov.hamrohtaxi.models.Region
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class NewPostFragment : Fragment(R.layout.fragment_new_post) {
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
                Log.d(TAG, "setupUI: clicked")
                var cal = Calendar.getInstance()
                val year = 2023
                val month = 5
                val day = 0
                val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd.MM.yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    // Display Selected date in textbox
                    Toast.makeText(requireContext(),sdf.format(cal.time), Toast.LENGTH_LONG).show()

                }, year, month, day)
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
                Toast.makeText(
                    requireContext(),
                    getString(R.string.selected_item) + " " + nameList[position],
                    Toast.LENGTH_SHORT
                ).show()

                callDistrictByID(regionId)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
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
                Toast.makeText(
                    requireContext(),
                    getString(R.string.selected_item) + " " + nameList[position],
                    Toast.LENGTH_SHORT
                ).show()

                callDistrictByID2(regionId)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Code to perform some action when nothing is selected
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
}