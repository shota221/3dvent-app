package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import jp.microvent.microvent.R
import jp.microvent.microvent.viewModel.MainViewModel


class        MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.mainFragment)
        val bottomNavigaton = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupWithNavController(bottomNavigaton, navController)

        mainViewModel.checkAppkey()
    }


}