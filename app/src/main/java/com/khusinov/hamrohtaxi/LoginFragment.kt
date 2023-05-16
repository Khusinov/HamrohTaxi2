package com.khusinov.hamrohtaxi

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


            loginBtn.setOnClickListener {

                val phone = phoneEt.text.toString()
                val password = passwordEt.text.toString()

                val authLogin = AuthLogin(phone, password)

                Common.retrofitServices.login(authLogin).enqueue(object : Callback<LoginResponse>{
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        Log.d(TAG, "onResponse: ${response.message()} ")
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.message}")
                    }

                })

            }


        }

    }

}