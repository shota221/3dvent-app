package jp.microvent.microvent.view.ui

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.drawerlayout.widget.DrawerLayout
import jp.microvent.microvent.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
open abstract class DrawableFragment : BaseFragment() {

    protected val drawerLayout: DrawerLayout by lazy {
        requireActivity().findViewById(R.id.drawerLayout)
    }

    var targetId: Int? = null

    override fun onStop() {
        super.onStop()
        drawerLayout.apply {
            targetId?.let {
                findViewById<ImageView>(it)?.apply {
                    setImageResource(R.drawable.flow_circle)
                }
            }
            setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    fun showFlowDrawer(flowNow: Int) {
        //該当ページ箇所を強調してドロワー有効化
        targetId = flowNow
        drawerLayout.apply {
            findViewById<ImageView>(flowNow).apply {
                setImageResource(R.drawable.flow_circle_now)
            }
            setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            openDrawer(Gravity.LEFT)
        }
    }
}