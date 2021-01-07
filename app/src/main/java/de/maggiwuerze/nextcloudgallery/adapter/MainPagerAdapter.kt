package de.maggiwuerze.nextcloudgallery.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import de.maggiwuerze.nextcloudgallery.fragments.AlbumsFragment
import de.maggiwuerze.nextcloudgallery.fragments.GalleryFragment
import de.maggiwuerze.nextcloudgallery.fragments.SettingsFragment

internal class MainPagerAdapter(fm: FragmentManager?) :
    FragmentPagerAdapter(fm!!) {

    var albumsFragment: AlbumsFragment? = null
    var galleryFragment: GalleryFragment? = null
    var settingsFragment: SettingsFragment? = null

    override fun getItem(position: Int): Fragment {

        return when (position) {

            0 -> {
                if (albumsFragment == null) {
                    albumsFragment = AlbumsFragment()
                }
                return albumsFragment!!
            }
            1 -> {
                if (galleryFragment == null) {
                    galleryFragment = GalleryFragment()
                }
                return galleryFragment!!
            }
            2 -> {
                if (settingsFragment == null) {
                    settingsFragment = SettingsFragment()
                }
                return settingsFragment!!
            }

            else -> AlbumsFragment()
        }

    }

    override fun getCount() = 3
}