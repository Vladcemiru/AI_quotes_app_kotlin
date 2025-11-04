package com.example.ai_quotes_app

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.ContentView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.ai_quotes_app.data.QuotesDatabase
import com.example.ai_quotes_app.data.QuotesViewModel
import com.example.ai_quotes_app.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    public val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            QuotesDatabase::class.java,
            "quotes.db"
        ).build()
    }
//    private val viewModel by viewModels<QuotesViewModel>(
//        factoryProducer = {
//            object: ViewModelProvider.Factory {
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return QuotesViewModel(db.dao) as T
//                }
//            }
//        }
//    )
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_dashboard, R.id.navigation_home, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }
}