package com.usualteam.eventsonthefields

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), DashboardFragment.DashboardInteractionListener, LogFragment.LogInteractionListener {

    private val dashboardFragment: DashboardFragment = DashboardFragment()
    private val logFragment: LogFragment = LogFragment()

    // BottomNavigationView: Dashboard + Log
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewPager(viewPager)
        tabLayout.setupWithViewPager(viewPager)
    }

    // инициализация viewPager-а
    private fun setupViewPager(viewPager: ViewPager){
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(dashboardFragment, "Dashboard")
        adapter.addFragment(logFragment, "Log")
        viewPager.adapter = adapter
        viewPager.currentItem = 0
    }
}
