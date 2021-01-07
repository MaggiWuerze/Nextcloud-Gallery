package de.maggiwuerze.nextcloudgallery.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import de.maggiwuerze.nextcloudgallery.fragments.DetailFragment

class DetailPagerAdapter(fragment: Fragment, var size: Int) :
    FragmentStatePagerAdapter(fragment.childFragmentManager) {

    override fun getCount() = size

    override fun getItem(position: Int): Fragment {
        return DetailFragment.newInstance(position)
    }
}