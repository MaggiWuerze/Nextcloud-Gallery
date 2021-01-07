package de.maggiwuerze.nextcloudgallery.activity

import android.Manifest
import android.animation.ObjectAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.adapter.MainPagerAdapter
import de.maggiwuerze.nextcloudgallery.persistence.model.DatabaseManager
import de.maggiwuerze.nextcloudgallery.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*

class Main : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var mainPagerAdapter: MainPagerAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private val PERMISSIONS_REQUEST_READ_STORAGE = 42

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_STORAGE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    TODO("Disable CardViews in DetailPanel")
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
        DatabaseManager.getInstance().initialize(this)

        viewPager = mainViewPager
        mainPagerAdapter = MainPagerAdapter(supportFragmentManager)
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = mainPagerAdapter

        var prefs = PreferenceManager.getDefaultSharedPreferences(this)

        var fragIndex = Integer.parseInt(prefs.getString("start_fragment", "0")!!)
//        var fragIndex = 0

        viewPager.currentItem = fragIndex //TODO: Setting for this
        changeTabs(fragIndex)

        albums_btn.setOnClickListener {
            viewPager.currentItem = 0 //TODO: Setting for this
            changeTabs(0)
        }
        photos_btn.setOnClickListener {
            viewPager.currentItem = 1 //TODO: Setting for this
            changeTabs(1)
        }
        settings_btn.setOnClickListener {
            viewPager.currentItem = 2 //TODO: Setting for this
            changeTabs(2)
        }

        //listenmer
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onPageSelected(position: Int) {
                changeTabs(position)
            }


        })

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_READ_STORAGE
            )
        }


    }

    fun animateNav(show: Boolean, button: View) {

        sharedViewModel.navVisible = show
        var newY = if (show) 0.0F else 300.0F

        ObjectAnimator.ofFloat(bottom_nav, "translationY", newY)
            .apply {
                duration = 200
                start()
            }
        ObjectAnimator.ofFloat(button, "translationY", newY)
            .apply {
                duration = 200
                start()
            }
    }

    private fun changeTabs(position: Int) {

        val scaledUp = 1.2f
        albums_btn.scaleX = 1F
        albums_btn.scaleY = 1F
        photos_btn.scaleX = 1F
        photos_btn.scaleY = 1F
        settings_btn.scaleX = 1F
        settings_btn.scaleY = 1F
        when (position) {

            0 -> {
                albums_btn.setImageResource(R.drawable.ic_home_accent)
                photos_btn.setImageResource(R.drawable.ic_dashboard_black)
                settings_btn.setImageResource(R.drawable.ic_settings_black)
                albums_btn.scaleX = scaledUp
                albums_btn.scaleY = scaledUp
            }
            1 -> {
                albums_btn.setImageResource(R.drawable.ic_home_black)
                photos_btn.setImageResource(R.drawable.ic_dashboard_accent)
                settings_btn.setImageResource(R.drawable.ic_settings_black)
                photos_btn.scaleX = scaledUp
                photos_btn.scaleY = scaledUp
            }
            2 -> {
                albums_btn.setImageResource(R.drawable.ic_home_black)
                photos_btn.setImageResource(R.drawable.ic_dashboard_black)
                settings_btn.setImageResource(R.drawable.ic_settings_accent)
                settings_btn.scaleX = scaledUp
                settings_btn.scaleY = scaledUp
            }

        }


    }
}
