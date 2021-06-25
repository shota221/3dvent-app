package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.view.Gravity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import jp.microvent.microvent.R
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.MainViewModel
import jp.microvent.microvent.viewModel.util.EventObserver


class MainActivity : AppCompatActivity(),
    DialogConnectionErrorFragment.DialogConnectionErrorListener {

    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var drawerLayout:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.mainFragment)
        val bottomNavigaton = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupWithNavController(bottomNavigaton, navController)

        mainViewModel.checkAppkey()

        drawerLayout = findViewById(R.id.drawerLayout)
        //ドロワー制御
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        mainViewModel.showDialogConnectionError.observe(
            this, EventObserver {
                val dialog = DialogConnectionErrorFragment()
                dialog.show(supportFragmentManager, it)
            }
        )

        mainViewModel.showFlowDrawer.observe(
            this, EventObserver {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
        )
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        mainViewModel.checkAppkey()
    }

    override fun onBackPressed() {
        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.mainFragment)?.childFragmentManager?.fragments?.first()
        if (fragment is OnBackKeyPressedListener) {
            fragment.onBackPressed()
        }
    }
}

interface OnBackKeyPressedListener {
    fun onBackPressed()
}

