package de.maggiwuerze.nextcloudgallery.ui.albums

import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.google.android.flexbox.FlexboxLayoutManager
import de.maggiwuerze.nextcloudgallery.persistence.model.photo.Photo
import kotlinx.android.synthetic.main.photo_card.view.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File


class GalleryViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    private val baseURl = "https://cloud.maggiwuerze.de"
    private val photoCardThumbnail: ImageView = view.photo_card_image
    private lateinit var photo: Photo
    val errorHandler = CoroutineExceptionHandler { context, error ->
    }

    fun bind(p: Photo) {

        photo = p
        val lp = photoCardThumbnail.layoutParams
        if (lp is FlexboxLayoutManager.LayoutParams) {
            lp.flexGrow = 1f
        }


        CoroutineScope(errorHandler).launch {
            loadImage(photo)
        }
    }

    private fun loadImage(photo: Photo) {

        // if no thumbnail is present create one and save it to disk and db
        var directory =
            ContextWrapper(itemView.context)
                .getDir("thumbs", Context.MODE_PRIVATE)
        var file = File(directory, "${photo.id}_thumbnail")

        // try to load thumbnail from disk
        photoCardThumbnail.load(BitmapDrawable.createFromPath(file.path))

    }


}