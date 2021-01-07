package de.maggiwuerze.nextcloudgallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.viewmodel.SharedViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumsFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albums, container, false)
    }

}