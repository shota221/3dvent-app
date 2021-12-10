package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.os.Process
import android.util.Log
import android.view.Gravity
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import jp.microvent.microvent.R
import jp.microvent.microvent.service.model.AppkeyFetchForm
import jp.microvent.microvent.service.repository.MicroventRepository
import jp.microvent.microvent.service.repository.SharedAccessTokenRepository
import jp.microvent.microvent.view.ui.dialog.DialogAppkeyCheckErrorFragment
import jp.microvent.microvent.view.ui.dialog.DialogConnectionErrorFragment
import jp.microvent.microvent.viewModel.MainViewModel
import jp.microvent.microvent.viewModel.util.EventObserver


class MainActivity : AppCompatActivity(),
    DialogAppkeyCheckErrorFragment.DialogAppkeyCheckErrorListener {


    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel.checkAppkey()

        mainViewModel.showAppkeyCheckError.observe(
            this, EventObserver {
                val dialog = DialogConnectionErrorFragment()
                dialog.show(supportFragmentManager, it)
            }
        )

        setContentView(R.layout.activity_main)
        setUpNavigation()

    }


    private fun setUpNavigation() {
        val navController = findNavController(R.id.mainFragment)

        /**
         * ツールバー設定
         */
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        //戻るボタンを表示させたくないfragmentを設定
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.qr_reading_fragment,
            R.id.patient_setting_fragment,
            R.id.support_fragment,
            R.id.patient_info_fragment,
            R.id.setting_fragment,
            R.id.auth_fragment,
            R.id.ventilator_setting_fragment,
            R.id.ventilator_result_fragment
        ).build()
        toolbar.setupWithNavController(navController, appBarConfiguration)

        /**
         * ドロワー制御
         * ドロワーはflowボタンを押されたときにのみ出現する
         * 通常はLOCKED_CLOSEDにしておく
         */
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        /**
         * ボトムナビゲーション設定
         */
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        setupWithNavController(bottomNavigation, navController)
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        mainViewModel.checkAppkey()
    }

    override fun onBackPressed() {
        val fragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.mainFragment)?.childFragmentManager?.fragments?.first()
        if (fragment is OnBackKeyPressedListener) {
            fragment.onBackPressed()
        }
    }
}

interface OnBackKeyPressedListener {
    fun onBackPressed()
}

