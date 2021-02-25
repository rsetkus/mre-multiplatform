package co.uk.mre

import android.util.Log

actual fun print(message: String) {
    Log.d("multiplatform", message)
}
