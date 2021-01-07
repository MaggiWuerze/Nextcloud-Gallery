package de.maggiwuerze.nextcloudgallery.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceFragmentCompat
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.viewmodel.OnboardingViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class OnboardingSettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}