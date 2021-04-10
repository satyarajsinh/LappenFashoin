package com.lappenfashion.ui

import android.annotation.SuppressLint
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
            setProfileColor()
            navController.navigate(R.id.nav_profile)
        }

        linearCategories.setOnClickListener {
            setCategoryColor()
            navController.navigate(R.id.nav_category)
        }

        linearStudio.setOnClickListener {
            setStudioColor()
            navController.navigate(R.id.nav_category)
        }

        linearExplore.setOnClickListener {
            setExploreColor()
            navController.navigate(R.id.nav_category)
        }

        linearHome.setOnClickListener {
            setHomeColor()
            navController.navigate(R.id.nav_home)
        }
    }

    private fun setExploreColor() {
        imgHome.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        imgCategory.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgStudio.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgExplore.setColorFilter(
            ContextCompat.getColor(this, R.color.colorAccent),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgProfile.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        txtHome.setTextColor(resources.getColor(R.color.black))
        txtCategory.setTextColor(resources.getColor(R.color.black))
        txtExplore.setTextColor(resources.getColor(R.color.colorAccent))
        txtStudio.setTextColor(resources.getColor(R.color.black))
        txtProfile.setTextColor(resources.getColor(R.color.black))
    }

    private fun setStudioColor() {
        imgHome.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        imgCategory.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgStudio.setColorFilter(
            ContextCompat.getColor(this, R.color.colorAccent),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgExplore.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgProfile.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        txtHome.setTextColor(resources.getColor(R.color.black))
        txtCategory.setTextColor(resources.getColor(R.color.black))
        txtExplore.setTextColor(resources.getColor(R.color.black))
        txtStudio.setTextColor(resources.getColor(R.color.colorAccent))
        txtProfile.setTextColor(resources.getColor(R.color.black))
    }

    private fun setProfileColor() {
        imgCategory.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        imgHome.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        imgStudio.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgExplore.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgProfile.setColorFilter(
            ContextCompat.getColor(this, R.color.colorAccent),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        txtHome.setTextColor(resources.getColor(R.color.black))
        txtCategory.setTextColor(resources.getColor(R.color.black))
        txtExplore.setTextColor(resources.getColor(R.color.black))
        txtStudio.setTextColor(resources.getColor(R.color.black))
        txtProfile.setTextColor(resources.getColor(R.color.colorAccent))
    }

    private fun setCategoryColor() {
      /*  val mIcon = ContextCompat.getDrawable(this, R.mipmap.ic_light_category)?.mutate()
        mIcon?.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        imgCategory.setImageDrawable(mIcon);*/
        imgCategory.setColorFilter(
            ContextCompat.getColor(this, R.color.colorAccent),
            PorterDuff.Mode.SRC_ATOP
        )

        imgHome.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            PorterDuff.Mode.SRC_ATOP
        )

        imgStudio.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgExplore.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgProfile.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        txtHome.setTextColor(resources.getColor(R.color.black))
        txtCategory.setTextColor(resources.getColor(R.color.colorAccent))
        txtExplore.setTextColor(resources.getColor(R.color.black))
        txtStudio.setTextColor(resources.getColor(R.color.black))
        txtProfile.setTextColor(resources.getColor(R.color.black))
    }

    @SuppressLint("ResourceType")
    public fun setHomeColor(){
        imgHome.setColorFilter(
            ContextCompat.getColor(this, R.color.colorAccent),
            PorterDuff.Mode.SRC_ATOP
        )

        imgCategory.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgStudio.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgExplore.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        imgProfile.setColorFilter(
            ContextCompat.getColor(this, R.color.black),
            android.graphics.PorterDuff.Mode.SRC_ATOP
        )

        txtHome.setTextColor(resources.getColor(R.color.colorAccent))
        txtCategory.setTextColor(resources.getColor(R.color.black))
        txtExplore.setTextColor(resources.getColor(R.color.black))
        txtStudio.setTextColor(resources.getColor(R.color.black))
        txtProfile.setTextColor(resources.getColor(R.color.black))
    }
}