package softxpert.movie.app.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.util.Log
import android.view.WindowMetrics


//width < 600 Compact , width < 840 Medium, width >= 840 Expanded
//height < 480 Compact, height < 900, height >= 900
fun getScreenSize(resources: Resources): Pair<Int, Int> {
    Log.d(
        "ScreenSize",
        "Width: ${resources.displayMetrics.widthPixels},Height: ${resources.displayMetrics.heightPixels}"
    )
    val width = (resources.displayMetrics.widthPixels / resources.displayMetrics.density).toInt()
    val height = (resources.displayMetrics.heightPixels / resources.displayMetrics.density).toInt()

    return Pair(width, height)
}

@SuppressLint("NewApi")
fun getScreenSize(resources: Resources, windowMetrics: WindowMetrics): Pair<Int, Int> {
    Log.d(
        "ScreenSize",
        "Width: ${windowMetrics.bounds.width()},Height: ${windowMetrics.bounds.height()}"
    )
    val width: Int = (windowMetrics.bounds.width() / resources.displayMetrics.density).toInt()
    val height: Int = (windowMetrics.bounds.height() / resources.displayMetrics.density).toInt()
    return Pair(width, height)
}
