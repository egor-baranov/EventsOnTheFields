package com.usualteam.eventsonthefields

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), DashboardFragment.DashboardInteractionListener,
    LogFragment.LogInteractionListener {

    private val dashboardFragment: DashboardFragment = DashboardFragment()
    private val logFragment: LogFragment = LogFragment()

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

            override fun onProviderDisabled(provider: String) { }

            override fun onProviderEnabled(provider: String) {
                try{
                    currentLocation = locationManager?.getLastKnownLocation(provider)
                } catch(e: SecurityException){ }
            }

            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }
        thread(start = true){
            val queue = Volley.newRequestQueue(this)
            queue.start()
            while(working){
                Thread.sleep(1000)
//                runOnUiThread { Toast.makeText(
//                    applicationContext, "x = ${currentLocation?.latitude}, y = ${currentLocation?.longitude}",
//                    Toast.LENGTH_LONG).show()
//                }
                // request
                if(currentLocation != null) {
                    val url =
                        "http://$ip/tick?id=$id&lat=${currentLocation?.latitude}&lng=${currentLocation?.longitude}"
                    val stringRequest = StringRequest(Request.Method.GET, url, Response.Listener<String> { response ->
                        // обработка response
                        val jsonProperties: JSONObject = JSONObject(response).getJSONObject("properties")
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext, "HP=${jsonProperties.getDouble("hp")}, " +
                                        "radiation=${jsonProperties.getDouble("radiation")}",
                                Toast.LENGTH_LONG
                            ).show()
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
        if(locationManager != null) locationManager?.removeUpdates(locationListener!!)
    }

    // инициализация viewPager-а
    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(dashboardFragment, "Dashboard")
        adapter.addFragment(logFragment, "Log")
        viewPager.adapter = adapter
        viewPager.currentItem = 0
    }

    override fun onDestroy() {
        super.onDestroy()
        working = false
    }
}
