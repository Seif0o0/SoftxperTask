package softxpert.movie.app.data.utils

import android.app.Application
import android.util.Log
import softxpert.movie.app.R
import softxpert.movie.app.utils.Constants

fun handleStatusCode(application: Application, errorCode: String): String {
    Log.d("StatusCodeHandler", "ErrorCode: $errorCode")
    return when (errorCode) {
        Constants.CONNECTION_ERROR_KEY -> application.getString(R.string.no_internet_connection)
        "401" -> application.getString(R.string.error_401_message)
        "500" -> application.getString(R.string.server_error_message)
        "404" -> "${application.getString(R.string.error_404_message)}\n${application.getString(R.string.something_went_wrong_try_again_later)}"
        else -> errorCode
    }
}