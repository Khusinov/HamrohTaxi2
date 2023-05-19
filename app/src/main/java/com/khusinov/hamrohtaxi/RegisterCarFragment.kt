package com.khusinov.hamrohtaxi

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.khusinov.hamrohtaxi.databinding.FragmentRegisterCarBinding
import com.khusinov.hamrohtaxi.models.Car
import com.khusinov.hamrohtaxi.models.CarResponse
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterCarFragment : Fragment(R.layout.fragment_register_car) {
    private val binding by viewBinding { FragmentRegisterCarBinding.bind(it) }
    var sharedPreferences: SharedPreferences? = null
    private val TAG = "RegisterCarFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            sharedPreferences = context?.getSharedPreferences("HamrohTaxi", 0)

            var token = sharedPreferences?.getString("access", "").toString()
            token = "Bearer $token"

            Log.d(TAG, "setupUI: token ${token.toString()}")


            saveBtn.setOnClickListener {
                val carNumber = carNumberEdt.text.toString()
                val carType = carType.text.toString()

                val car = Car()
                car.car_model = carType
                car.car_number = carNumber



                Common.retrofitServices.createCar(car, token)
                    .enqueue(object : Callback<CarResponse> {
                        override fun onResponse(
                            call: Call<CarResponse>,
                            response: Response<CarResponse>
                        ) {
                            Log.d(TAG, "onResponse: car created ${response.message()}")
                            Toast.makeText(requireContext(), "Saqlandi", Toast.LENGTH_SHORT).show()
                            findNavController().navigate(R.id.action_registerCarFragment2_to_homeFragment)
                        }

                        override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                            Log.d(TAG, "onFailure: ${t.message}")
                            Toast.makeText(
                                requireContext(),
                                "Internetni tekshiring...",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    })

            }


        }
    }

}