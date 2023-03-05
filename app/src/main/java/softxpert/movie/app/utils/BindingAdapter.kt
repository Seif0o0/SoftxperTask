package softxpert.movie.app.utils

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.makeramen.roundedimageview.RoundedImageView
import softxpert.movie.app.R

@BindingAdapter("image_url", "image_size")
fun RoundedImageView.loadImage(url: String?, size: String) {
    Glide.with(context).load("${Constants.IMAGES_BASE_URL}$size$url")
        .placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground)
        .into(this)
}



@BindingAdapter("is_clicked")
fun MaterialCardView.isGenreClicked(isClicked: Boolean) {
    if (isClicked) {
        setCardBackgroundColor(
            ContextCompat.getColor(
                context, R.color.main_buttons_background_color
            )
        )
    } else {
        setCardBackgroundColor(
            ContextCompat.getColor(
                context, R.color.un_clicked_buttons_background_color
            )
        )
    }
}

@BindingAdapter("is_clicked")
fun MaterialTextView.isGenreClicked(isClicked: Boolean) {
    if (isClicked) {
        setTextColor(ContextCompat.getColor(context, R.color.main_background_color))
    } else {
        setTextColor(ContextCompat.getColor(context, R.color.titles_text_color))
    }
}


@BindingAdapter("loading_status")
fun LottieAnimationView.setLoadingStatus(isLoading: Boolean) {
    if (isLoading) {
        visibility = View.VISIBLE
        setAnimation("progress_bar.json")
        playAnimation()
        repeatCount = LottieDrawable.INFINITE
    } else {
        visibility = View.GONE
        cancelAnimation()
    }
}

@BindingAdapter("loading_status")
fun FrameLayout.setLoadingStatus(isLoading: Boolean) {
    visibility = if (isLoading) View.VISIBLE
    else View.GONE
}

@BindingAdapter("error_status")
fun ConstraintLayout.setErrorStatus(isError: Boolean) {
    visibility = if (isError) View.VISIBLE
    else View.GONE
}

@BindingAdapter("error_status", "error_message")
fun LottieAnimationView.setErrorStatus(isError: Boolean, errorMessage: String) {
    if (isError) {
        setAnimation(if (errorMessage == context.getString(R.string.no_internet_connection)) "no_internet_connection.json" else "error_dialog_animation.json")
        playAnimation()
        repeatCount = LottieDrawable.INFINITE
    } else {
        cancelAnimation()
    }
}

@BindingAdapter("rate_visibility")
fun Group.setGroupVisibility(rate: Float) {
    visibility = if (rate > 0) View.VISIBLE else View.GONE
}

@BindingAdapter("release_date")
fun MaterialTextView.setReleaseYear(date:String?){
    date?.let { text = "(${it.split("-")[0]})" }

}