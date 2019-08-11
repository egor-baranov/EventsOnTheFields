package com.usualteam.eventsonthefields

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dashboard.*
import org.json.JSONArray
import org.json.JSONObject
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), DashboardFragment.DashboardInteractionListener,
    LogFragment.LogInteractionListener, EffectsFragment.EffectsInteractionListener {

    private val dashboardFragment: DashboardFragment = DashboardFragment()
    private val logFragment: LogFragment = LogFragment()
    private val effectsFragment: EffectsFragment = EffectsFragment()

    var locationManager: LocationManager? = null
    var locationListener: LocationListener? = null
    var currentLocation: Location? = null
    val ip = "192.168.43.3:5000"
    var id: Long = -1
    var name: String = "username"
    var working = true
    // BottomNavigationView: Dashboard + Log
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openFileInput("id").use {
            id = it.readBytes().toString(Charsets.UTF_8).toLong()
        }
        openFileInput("name").use {
            name = it.readBytes().toString(Charsets.UTF_8)
        }
        // Toast.makeText(this, "$name, твой id = $id", Toast.LENGTH_LONG).show()
        setContentView(R.layout.activity_main)
        setupViewPager(viewPager)
        tabLayout.run {
            setupWithViewPager(viewPager)

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab) {}
                override fun onTabUnselected(tab: TabLayout.Tab) {}
                override fun onTabReselected(tab: TabLayout.Tab) {}
            })
        }

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                currentLocation = location
            }

            override fun onProviderDisabled(provider: String) {}

            override fun onProviderEnabled(provider: String) {
                try {
                    currentLocation = locationManager?.getLastKnownLocation(provider)
                } catch (e: SecurityException) {
                }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }
        thread(start = true) {
            val queue = Volley.newRequestQueue(this)
            queue.start()
            while (working) {
                Thread.sleep(1000)
                runOnUiThread{ effectsFragment.clearGrid() }
                if (currentLocation != null) {
                    val url =
                        "http://$ip/tick?id=$id&lat=${currentLocation?.latitude}&lng=${currentLocation?.longitude}"
                    val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                        // обработка response

                        val jsonProperties: JSONObject = JSONObject(response).getJSONObject("properties")
                        val jsonContinuousEvents: JSONArray = JSONObject(response).getJSONArray("continuous_events")
                        val jsonOneTimeEvents: JSONArray = JSONObject(response).getJSONArray("one_time_events")

                        runOnUiThread {
                            Toast.makeText(
                                applicationContext, "HP=${jsonProperties.getDouble("hp")}, " +
                                        "radiation=${jsonProperties.getDouble("radiation")}",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                        hpIndicator.progress = round(jsonProperties.getDouble("hp"))
                        dashboardFragment.addProperty("radiation", round(jsonProperties.getDouble("radiation")))

                        // итерация по контам
                        for(i in 0 until jsonContinuousEvents.length()){
                            val curObj = jsonContinuousEvents.getJSONObject(i)
                            effectsFragment.addContinuousEvent(curObj.getString("name"), curObj.getString("type"))
                        }

                        // итерация по onetime-ам
                        for(i in 0 until jsonOneTimeEvents.length()){
                            val curObj = jsonOneTimeEvents.getJSONObject(i)
                            Log.d("DEBUG", curObj.toString())
                            logFragment.addOneTimeEvent(curObj.getString("name"), curObj.getString("type"),
                                curObj.getDouble("timestamp").toLong())
                        }

                    }, Response.ErrorListener { err ->
                        Log.d("DEBUG", err.toString())
                    })
                    queue.add(stringRequest)
                }
            }
            queue?.cancelAll("A")
        }
    }

    private fun round(n: Double): Int = n.toInt() + if (n == n.toInt().toDouble()) 0 else 1

    override fun onResume() {
        super.onResume()
        if (locationListener == null) return
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager?.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0.1F, locationListener!!
            )
            locationManager?.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0.1F, locationListener!!
            )
        } else {
            if (ContextCompat.checkSelfPermission(
                    applicationContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), 1
                )

            if (ContextCompat.checkSelfPermission(
                    applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            )
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
        }
    }

    override fun onPause() {
        super.onPause()
        if (locationManager != null) locationManager?.removeUpdates(locationListener!!)
    }

    // инициализация viewPager-а
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(effectsFragment, "Effects")
        adapter.addFragment(dashboardFragment, "Dashboard")
        adapter.addFragment(logFragment, "Log")
        viewPager.adapter = adapter
        viewPager.currentItem = 1
    }

    override fun onDestroy() {
        super.onDestroy()
        working = false
    }
}
