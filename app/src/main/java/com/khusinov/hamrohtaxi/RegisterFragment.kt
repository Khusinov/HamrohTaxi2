package com.khusinov.hamrohtaxi

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.khusinov.hamrohtaxi.databinding.FragmentRegisterBinding
import com.khusinov.hamrohtaxi.models.LoginResponse
import com.khusinov.hamrohtaxi.models.User
import com.khusinov.hamrohtaxi.models.UserCrud
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding { FragmentRegisterBinding.bind(it) }
    private val TAG = "RegisterFragment"
    var sharedPreferences: SharedPreferences? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            sharedPreferences = context?.getSharedPreferences("HamrohTaxi", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()


            btnRegister.setOnClickListener {

                val username = usernameEt.text.toString()
                val phone = "+998" + phoneNumberEt.text.toString()
                val password = passwordEt2.text.toString()
                val passanger = passanger.isChecked
                var driver = driver.isChecked
                val userCrud = UserCrud()
                userCrud.name = username
                userCrud.password = password
                userCrud.phone_number = phone
                userCrud.user_role = if (passanger) 0 else 1

                Common.retrofitServices.signup(userCrud).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        Log.d(TAG, "onResponse: ${response.message()}")

                        editor?.putString("access", response.body()?.access)
                        editor?.apply()

                        if (driver) {
                            findNavController().navigate(R.id.action_registerFragment2_to_registerCarFragment2)
                        } else {
                            findNavController().navigate(R.id.action_registerFragment2_to_homeFragment)
                        }

                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }

                })

                if (passanger) {

                } else {

                }

            }

        }
    }

}