package jp.microvent.microvent.view.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.microvent.microvent.R
import jp.microvent.microvent.viewModel.AuthViewModel

class        MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.mainFragment)
        val bottomNavigaton = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupWithNavController(bottomNavigaton,navController)
    }
}