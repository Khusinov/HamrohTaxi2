package com.khusinov.hamrohtaxi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.khusinov.hamrohtaxi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navController = findNavController(R.id.nav_host_fragment)

        navController.setGraph(R.navigation.nav_graph)


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                    true
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.chatFragment -> {
                    navController.navigate(R.id.chatFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    binding.bottomNavigationView.visible(true)
                }
                R.id.searchFragment -> {
                    binding.bottomNavigationView.visible(true)
                }
                R.id.chatFragment -> {
                    binding.bottomNavigationView.visible(true)
                }
                R.id.profileFragment -> {
                    binding.bottomNavigationView.visible(true)
                }

            }
        }
    }
}

private fun BottomNavigationView.visible(b: Boolean) {
    this.visibility = if (b) View.VISIBLE else View.GONE
}
