package com.khusinov.hamrohtaxi

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.khusinov.hamrohtaxi.databinding.FragmentLoginBinding
import com.khusinov.hamrohtaxi.models.AuthLogin
import com.khusinov.hamrohtaxi.models.LoginResponse
import com.khusinov.hamrohtaxi.network.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding { FragmentLoginBinding.bind(it) }
    private val TAG = "LoginFragment"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {

            val sharedPref = context?.getSharedPreferences("HamrohTaxi", Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()

            loginBtn.setOnClickListener {

                val phone = "+998" + phoneEt.text.toString()
                val password = passwordEt.text.toString()

                val authLogin = AuthLogin(phone, password)


                Common.retrofitServices.login(authLogin).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        Log.d(TAG, "onResponse: ${response.message()} ")
                        if (response.isSuccessful && response.body()?.access != null) {
                            editor?.putString("access", response.body()?.access)
                            editor?.apply()

                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }

                })

            }


        }

    }

}