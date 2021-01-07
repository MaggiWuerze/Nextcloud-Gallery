package de.maggiwuerze.nextcloudgallery.fragments

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.github.chrisbanes.photoview.OnPhotoTapListener
import com.github.chrisbanes.photoview.OnSingleFlingListener
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.ViewItem
import de.maggiwuerze.nextcloudgallery.dialog.DetailBottomSheetDialog
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo
import de.maggiwuerze.nextcloudgallery.util.extensions.toggle
import de.maggiwuerze.nextcloudgallery.util.md5
import de.maggiwuerze.nextcloudgallery.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail_pager.*
import java.io.File
import kotlin.math.abs


class DetailFragment : Fragment() {

    private val DEBUG_TAG = "DETAIL_ACTIVITY"
    private val baseURl = "https://cloud.maggiwuerze.de"
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var dialog: DetailBottomSheetDialog
    private lateinit var photo: Photo
    var imageRes: Int = -1

    companion object {

        fun newInstance(posInt: Int): DetailFragment {
            val fragment = DetailFragment()
            fragment.imageRes = posInt
            return fragment
        }

    }

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
    ): View? = inflater.inflate(R.layout.fragment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photo =
            imageRes.let {
                var item = sharedViewModel.gallery.value?.get(it)
                when (item) {
                    is ViewItem.ImageItem -> item.photo
                    is ViewItem.DateItem -> item.photo
                    is ViewItem.NameItem -> item.photo
                    else -> null
                }
            }!!

        dialog = DetailBottomSheetDialog(context = this.requireContext(), photo = photo)

        var directory =
            ContextWrapper(this.requireContext())
                .getDir("thumbs", Context.MODE_PRIVATE)
        var file = File(directory, photo.url.md5())

        val exifInterface = ExifInterface(file.path)
        val make = exifInterface.getAttribute(ExifInterface.TAG_MAKE)
        val model = exifInterface.getAttribute(ExifInterface.TAG_MODEL)
        val latlong = exifInterface.latLong
        val resX = exifInterface.getAttribute(ExifInterface.TAG_X_RESOLUTION)
        val resY = exifInterface.getAttribute(ExifInterface.TAG_Y_RESOLUTION)
        val focal = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)

        // try to load thumbnail from disk
        detail_image.load(baseURl + photo.url) {
            if (file.exists()) {
                placeholder(BitmapDrawable.createFromPath(file.path))
            }
        }
        detail_image.setOnPhotoTapListener(PhotoTapListener())
        detail_image.setOnSingleFlingListener(SingleFlingListener(this.requireActivity()))
    }

    private inner class SingleFlingListener(var activity: FragmentActivity) :
        OnSingleFlingListener {

        override fun onFling(
            event1: MotionEvent,
            event2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            var deltaH = abs(event1.x - event2.x)
            var deltaV = abs(event1.y - event2.y)

            if (deltaH < deltaV) {
                if (event1.y > event2.y) {
                    //IS VERTICAL SWIPE UP
                    dialog.show(photo = photo)
                } else {
                    //IS VERTICAL SWIPE DOWN
                    parentFragment?.fragmentManager?.popBackStackImmediate()
                }
            }

            return true

        }
    }

    private inner class PhotoTapListener : OnPhotoTapListener {
        override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
            parentFragment?.detail_back_btn?.toggle()
            parentFragment?.detailpager_bottombar?.toggle()
        }
    }

}


