package de.maggiwuerze.nextcloudgallery.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.transition.TransitionInflater
import androidx.viewpager.widget.ViewPager
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.adapter.DetailPagerAdapter
import de.maggiwuerze.nextcloudgallery.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail_pager.*


class DetailPagerFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_detail_pager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onboarding_pager.adapter = DetailPagerAdapter(this, sharedViewModel.gallery.value!!.size)
        // Set the current position and add a listener that will update the selection coordinator when
        // paging the images.
        onboarding_pager.currentItem = sharedViewModel.currentPosition

        onboarding_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                sharedViewModel.currentPosition = position
            }
        })
        detail_back_btn.setOnClickListener {
            fragmentManager?.popBackStackImmediate()
        }

//        prepareSharedElementTransition()

        // Avoid a postponeEnterTransition on orientation change, and postpone only of first creation.
//        if (savedInstanceState == null) {
//            postponeEnterTransition()
//        }
    }

    /**
     * Prepares the shared element transition from and back to the grid fragment.
     */
    private fun prepareSharedElementTransition() {
        var transition =
            TransitionInflater.from(context)
                .inflateTransition(R.transition.image_shared_element_transition)
        transition.duration = 325

        sharedElementEnterTransition = transition

        // A similar mapping is set at the GridFragment with a setExitSharedElementCallback.
        setEnterSharedElementCallback(
            object : SharedElementCallback() {
                override fun onMapSharedElements(
                    names: MutableList<String>?,
                    sharedElements: MutableMap<String, View>?
                ) {
                    // Locate the image view at the primary fragment (the ImageFragment that is currently
                    // visible). To locate the fragment, call instantiateItem with the selection position.
                    // At this stage, the method will simply return the fragment at the position and will
                    // not create a new one.
                    var currentFragment: Fragment =
                        onboarding_pager.adapter?.instantiateItem(
                            onboarding_pager,
                            sharedViewModel.currentPosition
                        ) as DetailFragment

                    var view: View? = currentFragment.view ?: return

                    // Map the first shared element name to the child ImageView.
                    sharedElements?.set(names!![0], view?.findViewById(R.id.image)!!)
                }
            })
    }
}
