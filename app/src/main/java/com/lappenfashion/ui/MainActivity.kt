package com.lappenfashion.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.google.android.material.navigation.NavigationView
import com.lappenfashion.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_bar.*

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )

        bottomNavigation()
        setHomeColor()
      /*  setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun bottomNavigation() {
        linearProfile.setOnClickListener {
            navController.navigate(R.id.nav_profile)
        }

        linearCategories.setOnClickListener {
            navController.navigate(R.id.nav_category)
        }

        linearStudio.setOnClickListener {
            navController.navigate(R.id.nav_category)
        }

        linearExplore.setOnClickListener {
            navController.navigate(R.id.nav_category)
        }

        linearHome.setOnClickListener {
            navController.navigate(R.id.nav_home)
        }
    }

    @SuppressLint("ResourceType")
    public fun setHomeColor(){
        imgHome.setColorFilter(
            ContextCompat.getColor(this, R.color.pink),
            android.graphics.PorterDuff.Mode.MULTIPLY
        )
        txtHome.setTextColor(resources.getColor(R.color.pink))

    }
}