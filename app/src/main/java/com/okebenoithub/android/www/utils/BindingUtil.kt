package com.okebenoithub.android.www.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.okebenoithub.android.www.R
import de.hdodenhof.circleimageview.CircleImageView

@BindingAdapter("photoImageCircular")
fun setImageCircular(view: CircleImageView, imagePath: String?) {
    imagePath?.let {
        Glide.with(view.context)
            .load(imagePath)
            .error(R.drawable.ic_baseline_photo_128)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .placeholder(ColorDrawable(Color.TRANSPARENT))
            .into(view)
    }
}