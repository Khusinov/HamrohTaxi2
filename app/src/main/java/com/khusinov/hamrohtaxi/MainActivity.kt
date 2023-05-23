package com.khusinov.hamrohtaxi

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.khusinov.hamrohtaxi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var sharedPreferences: SharedPreferences? = null
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("HamrohTaxi", 0)
        val status = sharedPreferences?.getString("login", "").toString()

        Log.d(TAG, "onCreate: $status ")


//        val navHostFragment =
//            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
//        binding.bottomNavigationView.setupWithNavController(navController)

        val navController = findNavController(R.id.nav_host_fragment)

        navController.setGraph(R.navigation.nav_graph)


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.chatFragment -> {
                    if (status == "logged") {
                        navController.navigate(R.id.chatFragment)
                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                    true
                }
                R.id.homeFragment -> {
                    if (status == "logged") {
                        navController.navigate(R.id.homeFragment)
                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                    true
                }

                R.id.searchFragment -> {
                    if (status == "logged") {
                        navController.navigate(R.id.searchFragment)
                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                    true
                }

                R.id.profileFragment -> {
                    if (status == "logged") {
                        navController.navigate(R.id.profileFragment)
                    } else {
                        navController.navigate(R.id.loginFragment)
                    }
                    true
                }
                else ->false


            }
        }


    }
}
