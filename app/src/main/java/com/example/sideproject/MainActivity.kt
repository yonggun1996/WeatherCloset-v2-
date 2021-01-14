package com.example.sideproject

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sideproject.ui.dashboard.DashboardFragment
import com.example.sideproject.ui.home.HomeFragment
import com.example.sideproject.ui.notifications.NotificationsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    private lateinit var dashboardFragment: DashboardFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var notificationsFragment: NotificationsFragment

    companion object{
        const val  TAG : String = "로그"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //val navView: BottomNavigationView = findViewById(R.id.nav_view)

        //val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(
           // setOf(
             //   R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            //)
        //)
        //setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)

        Log.d(TAG,"MainActivity - onCreate()")
        nav_view.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)

        homeFragment = HomeFragment.newInstance()
        supportFragmentManager.beginTransaction().add(R.id.nav_host_fragment, homeFragment).commit()
    }

    private val onBottomNavItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when(it.itemId){
            R.id.navigation_dashboard -> {
                Log.d(TAG,"대시보드")
                dashboardFragment = DashboardFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, dashboardFragment).commit()
            }
            R.id.navigation_home -> {
                Log.d(TAG,"대시보드")
                homeFragment = HomeFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, homeFragment).commit()
            }
            R.id.navigation_notifications -> {
                Log.d(TAG,"대시보드")
                notificationsFragment = NotificationsFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment, notificationsFragment).commit()
            }
        }

        true
    }
}
