package com.okebenoithub.android.www.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import ja.burhanrashid52.photoeditor.PhotoEditor
import ja.burhanrashid52.photoeditor.PhotoEditorView
import java.io.File
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Photo util :: contain every recurring task dealing with Photo
 */
class PhotoUtil @Inject constructor() {
    /**
     * Create a new photo file
     * @return :: nothing
     */
    @Throws(IOException::class)
    fun createImageFile(context: Context, filePathPrefExtra: String): File {
        // Create a photo file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",  /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            SharedPrefUtil().writeDataStringToSharedPreferences(
                context,
                filePathPrefExtra,
                absolutePath
            )
        }
    }

    /**
     * Get photo file path from shared preferences
     * @return :: photo file path from preferences
     */
    private fun getPhotoFilePath(context: Context, filePathPrefExtra: String): String? {
        return SharedPrefUtil().getDataStringFromSharedPreferences(context, filePathPrefExtra)
    }

    /**
     * load photo file with Glide
     * @param context :: context
     * @param photoFilePathUri :: photo file path URI
     * @param photoFilePathUrl :: photo file path URl
     * @param photoFileHolder :: photo image holder
     * @param errorDrawableImgFailed :: error drawable image failed
     */
    fun loadPhotoFileWithGlide(
        context: Context?,
        photoFilePathUri: String?,
        photoFilePathUrl: String?,
        photoFileHolder: ImageView?,
        errorDrawableImgFailed: Int,
        asBitmap: Bitmap? = null
    ) {
        if (photoFileHolder != null) {
            if (photoFilePathUri != null) {
                // if photo file path URI is not null => load photo from device
                if (asBitmap == null) {
                    Glide.with(context!!)
                        .load(File(photoFilePathUri))
                        .error(errorDrawableImgFailed)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(ColorDrawable(Color.TRANSPARENT))
                        .into(photoFileHolder)
                } else {
                    Glide.with(context!!)
                        .asBitmap()
                        .load(File(photoFilePathUri))
                        .error(errorDrawableImgFailed)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(ColorDrawable(Color.TRANSPARENT))
                        .into(object : CustomTarget<Bitmap>(){
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                photoFileHolder.setImageBitmap(resource)
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            } else {
                // load photo from external url
                if (asBitmap == null) {
                    Glide.with(context!!)
                        .load(photoFilePathUrl)
                        .error(errorDrawableImgFailed)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(ColorDrawable(Color.TRANSPARENT))
                        .into(photoFileHolder)
                } else {
                    Glide.with(context!!)
                        .asBitmap()
                        .load(photoFilePathUrl)
                        .error(errorDrawableImgFailed)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(ColorDrawable(Color.TRANSPARENT))
                        .into(object : CustomTarget<Bitmap>(){
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                photoFileHolder.setImageBitmap(resource)
                            }
                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            }
        }
    }

    /**
     * Photo editor view
     * @param context :: context
     * @param photoIntentUri :: Uri of the photo file
     * @param photoEditorView :: photo editor view
     * @return :: Photo editor object
     */
    fun photoEditorView(
        context: Context?,
        photoIntentUri: Uri?,
        photoEditorView: PhotoEditorView?
    ): PhotoEditor? {
        if (photoEditorView != null) {
            photoEditorView.source.adjustViewBounds = true
            photoEditorView.source.scaleType = ImageView.ScaleType.FIT_CENTER
            photoEditorView.source.setImageURI(photoIntentUri)
            // Photo editor
            return PhotoEditor.Builder(context!!, photoEditorView)
                .setPinchTextScalable(true)
                .build()
        }
        return null
    }

    // on saved photo file callback
    interface MyCallback {
        fun onSavedPhotoFile(isSuccessful: Boolean, editedPhotoPath: String?)
    }

    /**
     * save photo file to user device
     * @param context :: context
     * @param photoEditor :: photo editor
     * @param myCallback :: call back method
     */
    fun savePhotoFile(context: Context?, filePathPrefExtra: String, photoEditor: PhotoEditor?, myCallback: MyCallback) {
        if (photoEditor != null) {
            try {
                createImageFile(context!!,filePathPrefExtra)
                // check for write permission granted
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    photoEditor.saveAsFile(getPhotoFilePath(context,filePathPrefExtra).toString(), object :
                        PhotoEditor.OnSaveListener {
                        override fun onSuccess(imagePath: String) {
                            myCallback.onSavedPhotoFile(true,imagePath)
                        }

                        override fun onFailure(exception: Exception) {
                            myCallback.onSavedPhotoFile(false,null)
                        }
                    })
                }
            } catch (e: IOException) {
                e.printStackTrace()
                myCallback.onSavedPhotoFile(false,null)
            }
        }
    }

    // Get colors palette from photo callback
    interface GetColorsPaletteFromPhotoCallback {
        fun onGetColors(dominantColor: Int, vibrantColor: Int, mutedColor: Int)
    }

    /**
     * Get colors palette from photo
     * @param context :: context
     * @param defaultColor :: default color
     * @param photoBitmap :: photo bitmap
     * @param getColorsPaletteFromPhotoCallback :: get colors palette from photo callback
     */
    fun getColorsPaletteFromPhoto(context: Context,defaultColor: Int, photoBitmap: Bitmap, getColorsPaletteFromPhotoCallback: GetColorsPaletteFromPhotoCallback) {
        Palette.Builder(photoBitmap).generate { it?.let {  palette ->
            val dominantColor = palette.getDominantColor(ContextCompat.getColor(context, defaultColor))
            val vibrantColor = palette.getVibrantColor(ContextCompat.getColor(context, defaultColor))
            val mutedColor = palette.getMutedColor(ContextCompat.getColor(context, defaultColor))
            getColorsPaletteFromPhotoCallback.onGetColors(dominantColor,vibrantColor,mutedColor)
        } }
    }

    /**
     * Get bitmap from photo drawable
     * @param context :: context
     * @param photoDrawable :: photo drawable
     */
    fun getBitmapFromPhotoDrawable(context: Context, photoDrawable: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, photoDrawable)
    }

    /**
     * Get bitmap from photo url
     * @param photoUrl :: photo url
     * @return :: photo bitmap
     */
    fun getBitmapFromPhotoUrl(photoUrl: String): Bitmap? {
        return try {
            val url = URL(photoUrl)
            BitmapFactory.decodeStream(url.openConnection().getInputStream())
        } catch (e: IOException) {
            null
        }
    }

    ///////////////////////////////////////// PHOTO SLIDER /////////////////////////////////////////////////

    /**
     * Photo slider view
     *
    fun photoSliderView(context: Context, photoItems: List<Photo>?): List<TextSliderView>? {
        val requestOptions = RequestOptions().fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(ColorDrawable(Color.GRAY))
            .error(R.mipmap.ic_launcher)

        // loop through list of photos
        if (photoItems != null) {
            if (photoItems.isNotEmpty()) {
                val sliderViews = ArrayList<TextSliderView>()
                for (photo in photoItems) {
                    val sliderView = TextSliderView(context)
                    // initialize SliderLayout
                    if (photo.absolutePath!!.contains("https://")) {
                        // slider from https images from url
                        sliderView
                            .image(photo.absolutePath)
                            .description(photo.caption)
                            .setRequestOption(requestOptions)
                            .setProgressBarVisible(true)
                    } else {
                        // slider from file image path
                        sliderView
                            .image(File(photo.absolutePath.toString()))
                            .description(photo.caption)
                            .setRequestOption(requestOptions)
                            .setProgressBarVisible(true)
                    }
                    sliderViews.add(sliderView)
                }
                return sliderViews
            }
        }
        return null
    }

    /**
     * Set random preset transformer to slider layout
     */
    fun setRandomPresetTransformersToSlider(photoSlider: SliderLayout?) {
        if (photoSlider != null) {
            val transformers = arrayOf(
                SliderLayout.Transformer.ZoomIn,
                SliderLayout.Transformer.ZoomOut,
                SliderLayout.Transformer.CubeIn,
                SliderLayout.Transformer.Accordion,
                SliderLayout.Transformer.Background2Foreground,
                SliderLayout.Transformer.DepthPage,
                SliderLayout.Transformer.FlipHorizontal,
                SliderLayout.Transformer.FlipPage,
                SliderLayout.Transformer.Default,
                SliderLayout.Transformer.RotateDown,
                SliderLayout.Transformer.RotateUp,
                SliderLayout.Transformer.Stack,
                SliderLayout.Transformer.Tablet,
                SliderLayout.Transformer.ZoomOutSlide
            )
            photoSlider.setPresetTransformer(transformers[Random().nextInt(transformers.size)])
        }
    }*/
}