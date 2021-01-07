package de.maggiwuerze.nextcloudgallery.ui.albums

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import coil.Coil
import coil.api.get
import coil.api.load
import de.maggiwuerze.nextcloudgallery.ViewItem
import de.maggiwuerze.nextcloudgallery.util.md5
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.date_seperator.view.*
import kotlinx.android.synthetic.main.photo_card.view.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.threeten.bp.format.DateTimeFormatter
import java.io.File
import java.io.FileOutputStream
import java.net.SocketTimeoutException
import java.util.*

class GalleryAdapter(private var galleryItems: ArrayList<ViewItem>, val context: Context) :
    Adapter<GalleryAdapter.ViewHolder>() {

    private val baseURl = "https://cloud.maggiwuerze.de"
    private val errorHandler = CoroutineExceptionHandler { _, error ->
        when (error) {
            is SocketTimeoutException -> {
                MainScope().launch {
                    Toast.makeText(
                        context,
                        "Could not reach Nextcloud Server",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
//        LayoutInflater.from(parent.context).inflate(R.layout.photo_card, parent, false)
//    )

    override fun getItemViewType(position: Int): Int {
        return galleryItems[position].resource
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(viewType, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (val item = galleryItems[position]) {
            is ViewItem.DateItem -> {
                val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())
                holder.itemView.date_separator_text.text = formatter.format(item.photo.date)
            }
            is ViewItem.NameItem -> {
                holder.itemView.date_separator_text.text =
                    item.photo.title[0].toString().toUpperCase()
            }
            is ViewItem.ImageItem -> {

                var directory =
                    ContextWrapper(context)
                        .getDir("thumbs", Context.MODE_PRIVATE)

                var hash = item.photo.url.md5()
                var file = File(directory, hash)

                // try to load thumbnail from disk
                if (file.exists()) {
                    holder.itemView.photo_card_image.load(BitmapDrawable.createFromPath(file.path))
                } else {
                    holder.itemView.photo_card_image.load(item.photo.previewUrl(baseURl))

                    CoroutineScope(errorHandler).launch {
                        var drawable = Coil.get(item.photo.previewUrl(baseURl))
                        FileOutputStream(file).use { out ->
                            drawable.toBitmap().compress(
                                Bitmap.CompressFormat.PNG,
                                100,
                                out
                            )
                        }
                    }

                }
            }
        }
    }

    fun updateImages(it: ArrayList<ViewItem>) {
        this.galleryItems = it
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = galleryItems.size

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer


}
