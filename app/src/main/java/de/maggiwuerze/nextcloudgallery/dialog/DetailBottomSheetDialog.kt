package de.maggiwuerze.nextcloudgallery.dialog

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.maggiwuerze.nextcloudgallery.R
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo
import kotlinx.android.synthetic.main.fragment_detail_bottom_sheet.view.*
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.threeten.bp.format.DateTimeFormatter


class DetailBottomSheetDialog(context: Context, var photo: Photo) : BottomSheetDialog(context) {

    private var view: View = setDetails(photo)

    init {
        Configuration.getInstance()
            .load(context, PreferenceManager.getDefaultSharedPreferences(context))
    }

    fun show(photo: Photo) {
        setContentView(setDetails(photo))
        super.show()
    }

    private fun setDetails(photo: Photo): View = LayoutInflater.from(context)
        .inflate(R.layout.fragment_detail_bottom_sheet, null).apply {


            if (detail_file_name != null) {
                detail_file_name.text = photo.title
                detail_file_path.text = photo.url
                detail_file_date.text =
                    photo.date.format(DateTimeFormatter.ofPattern("dd.MMMM yyyy"))
                if (Build.VERSION.SDK_INT >= 26) {
                    detail_file_name.tooltipText = photo.title
                    detail_file_path.tooltipText = photo.url
                    detail_file_date.tooltipText =
                        photo.date.format(DateTimeFormatter.ofPattern("dd.MMMM yyyy"))
                }

                detail_file_name.postDelayed({ isSelected = true }, 1000)
                detail_file_path.postDelayed({ isSelected = true }, 1000)
                detail_file_date.postDelayed({ isSelected = true }, 1000)

            }
            try {
                var mapController: IMapController =
                    mapView.controller
                var startPoint = GeoPoint(37.7749, -122.4192)
                mapView.zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
                mapView.setMultiTouchControls(false)
                mapController.setZoom(15.0)

                val startMarker = Marker(mapView).apply {
                    icon = resources.getDrawable(R.drawable.ic_location_on_black_24dp, null)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    position = startPoint
                }
                mapView.overlays.add(startMarker)
                mapController.setCenter(startPoint)

                mapLayout.setOnClickListener {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("geo:${startPoint.longitude},${startPoint.latitude}")
                    )
                    intent.resolveActivity(context.packageManager)
                        ?: startActivity(
                            context,
                            intent,
                            null
                        )
                }

            } catch (e: Exception) {
                Log.e("", e.localizedMessage)
            }

        }
}