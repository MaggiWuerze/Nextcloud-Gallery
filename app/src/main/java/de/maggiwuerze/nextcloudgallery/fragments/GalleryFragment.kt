package de.maggiwuerze.nextcloudgallery.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_SETTLING
import com.google.android.flexbox.*
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.ViewItem
import de.maggiwuerze.nextcloudgallery.activity.Main
import de.maggiwuerze.nextcloudgallery.ui.albums.GalleryAdapter
import de.maggiwuerze.nextcloudgallery.util.ClickListener
import de.maggiwuerze.nextcloudgallery.util.RecyclerTouchListener
import de.maggiwuerze.nextcloudgallery.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_gallery.*
import org.threeten.bp.format.DateTimeFormatter

class GalleryFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private val DEBUG_TAG = "GalleryFragment"
    private lateinit var bottomNavBar: LinearLayout
    private var bottomNavBarY = 0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        activity?.let {
            sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
        }
        bottomNavBar = (activity as Main).bottom_nav
        bottomNavBarY = bottomNavBar.translationY

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gallery, container, false)
    }

    // populate the views now that the layout has been inflated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!sharedViewModel.navVisible) {
            (activity as Main).animateNav(show = true, button = searchPhotoBtn)
        }
        photos_list.apply {
            adapter = GalleryAdapter(ArrayList(), this.context)
            layoutManager = FlexboxLayoutManager(this.context).apply {
                flexWrap = FlexWrap.WRAP
                flexDirection = FlexDirection.ROW
                alignItems = AlignItems.STRETCH
                justifyContent = JustifyContent.SPACE_EVENLY
            }
            addOnItemTouchListener(
                RecyclerTouchListener(
                    this.context!!,
                    photos_list, object : ClickListener {

                        override fun onClick(view: View?, position: Int) {
                            when (sharedViewModel.gallery.value!![position]) {
                                is ViewItem.ImageItem -> {
                                    sharedViewModel.currentPosition = position
                                    fragmentManager!!
                                        .beginTransaction()
                                        .setReorderingAllowed(true) // setAllowOptimization before 26.1.0
                                        .addSharedElement(view!!, view.transitionName)
                                        .replace(
                                            R.id.gallery_fragment,
                                            DetailPagerFragment(),
                                            DetailPagerFragment::class.java.simpleName
                                        )
                                        .addToBackStack(null)
                                        .commit()
                                    (activity as Main).animateNav(
                                        show = false,
                                        button = searchPhotoBtn
                                    )
                                }
                            }
                        }

                        override fun onLongClick(view: View?, position: Int) {
                            val item = sharedViewModel.gallery.value!![position]
                            if (item is ViewItem.ImageItem) {
                                var photo = item.photo
                                Log.d(
                                    "TAG",
                                    "click on photo at position [${position}]. " +
                                            "id=${photo.id}, " +
                                            "date=${photo.date.format(DateTimeFormatter.ofPattern("MMMM yyyy"))}, " +
                                            "previewUrl=${photo.previewUrl("https://cloud.maggiwuerze.de")}, " +
                                            "remoteUrl=${photo.url}"
                                )
                            }

                        }

                    })
            )
            addOnScrollListener(object : OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    if (bottomNavBar != null) {
                        if (dy > 0 && recyclerView.scrollState == SCROLL_STATE_SETTLING) {
                            Log.d(DEBUG_TAG, "showing nav")
                            (activity as Main).animateNav(show = false, button = searchPhotoBtn)
                        }
                        if (dy < 0 && recyclerView.scrollState == SCROLL_STATE_SETTLING) {
                            Log.d(DEBUG_TAG, "hiding nav")
                            (activity as Main).animateNav(show = true, button = searchPhotoBtn)
                        }
                    }
                }
            })
        }

        sharedViewModel.gallery.observe(this, albumListUpdateObserver)
    }

    private val albumListUpdateObserver: Observer<List<ViewItem>> = Observer {

        if (it.isNotEmpty()) {
            images_text.visibility = View.GONE
            images_bg.visibility = View.GONE
        }

        (photos_list.adapter as GalleryAdapter).updateImages(sharedViewModel.gallery.value!!)

    }
}