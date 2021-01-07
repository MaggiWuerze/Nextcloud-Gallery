package de.maggiwuerze.nextcloudgallery.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.fragments.onboarding.LoginFragment
import de.maggiwuerze.nextcloudgallery.fragments.onboarding.OnboardingSettingsFragment
import de.maggiwuerze.nextcloudgallery.fragments.onboarding.OnboardingTreeFragment
import de.maggiwuerze.nextcloudgallery.fragments.onboarding.LoadingFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class OnboardingPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3
    )

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> LoginFragment.newInstance(position + 1)
            1 -> OnboardingTreeFragment.newInstance(position + 1)
            2 -> OnboardingSettingsFragment()
            else -> LoadingFragment.newInstance(position + 1)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 4
    }
}