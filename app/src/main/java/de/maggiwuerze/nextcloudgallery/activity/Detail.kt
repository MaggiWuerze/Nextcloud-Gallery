//package de.maggiwuerze.nextcloudgalllery.activity
//
//import android.content.Context
//import android.content.ContextWrapper
//import android.graphics.drawable.BitmapDrawable
//import android.os.Bundle
//import android.util.Log
//import android.view.MotionEvent
//import android.view.View
//import android.widget.ImageView
//import androidx.fragment.app.FragmentActivity
//import coil.api.load
//import com.github.chrisbanes.photoview.OnPhotoTapListener
//import com.github.chrisbanes.photoview.OnSingleFlingListener
//import de.maggiwuerze.nextcloudgallery.R
//import de.maggiwuerze.nextcloudgallery.model.album.AccountRepository
//import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo
//import de.maggiwuerze.nextcloudgallery.persistence.model.photo.PhotoRepository
//import de.maggiwuerze.nextcloudgallery.dialog.DetailBottomSheetDialog
//import de.maggiwuerze.nextcloudgallery.util.md5
//import de.maggiwuerze.nextcloudgalllery.R
//import java.io.File
//import kotlin.math.abs
//
//
//class Detail : FragmentActivity() {
//
//    private val DEBUG_TAG = "DETAIL_ACTIVITY"
//    private val baseURl = "https://cloud.maggiwuerze.de"
//    private lateinit var accRepo: AccountRepository
//    private lateinit var photoRepo: PhotoRepository
//    private lateinit var photo: Photo
//    private lateinit var dialog: DetailBottomSheetDialog
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_detail)
//
//        photo = intent.getParcelableExtra<Photo>(PHOTO_PARCEL)
//        dialog = DetailBottomSheetDialog(this, photo)
//        var directory =
//            ContextWrapper(this)
//                .getDir("thumbs", Context.MODE_PRIVATE)
//        var file = File(directory, photo.url.md5())
//
//        // try to load thumbnail from disk
//        detail_image.load(baseURl + photo.url) {
//            if (file.exists()) {
//                placeholder(BitmapDrawable.createFromPath(file.path))
//            }
//        }
//        detail_image.setOnPhotoTapListener(PhotoTapListener())
//        detail_image.setOnSingleFlingListener(SingleFlingListener(this))
//        detail_back_btn.setOnClickListener { finishAfterTransition() }
//
//    }
//
//    companion object {
//        const val PHOTO_PARCEL = "PHOTO_ID"
//    }
//
//    private inner class SingleFlingListener(var activity: FragmentActivity) :
//        OnSingleFlingListener {
//
//        override fun onFling(
//            event1: MotionEvent,
//            event2: MotionEvent,
//            velocityX: Float,
//            velocityY: Float
//        ): Boolean {
//
//            var deltaH = abs(event1.x - event2.x)
//            var deltaV = abs(event1.y - event2.y)
//
//            if (deltaH > deltaV) {
//                if (event1.x < event2.x) {
//                    //IS HORIZONTAL SWIPE -->
//                    Log.d(DEBUG_TAG, "onFling: RIGHT")
////                    TODO("swipe to previous image")
//                } else {
//                    //IS HORIZONTAL SWIPE <--
//                    Log.d(DEBUG_TAG, "onFling: LEFT")
////                    TODO("swipe to next image")
//                }
//
//            } else {
//
//                if (event1.y > event2.y) {
//                    //IS VERTICAL SWIPE UP
//                    dialog.show()
//                } else {
//                    //IS VERTICAL SWIPE DOWN
//                    finishAfterTransition()
//                }
//
//            }
//
//            return true
//
//        }
//    }
//
//    private inner class PhotoTapListener : OnPhotoTapListener {
//
//        override fun onPhotoTap(view: ImageView?, x: Float, y: Float) {
//            if (detail_back_btn.visibility == View.VISIBLE) {
//                detail_back_btn.visibility = View.INVISIBLE
//            } else {
//                detail_back_btn.visibility = View.VISIBLE
//            }
//        }
//    }
//
//}
//
