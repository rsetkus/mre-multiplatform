package co.uk.mre

import platform.Foundation.NSLog

actual fun print(message: String) {
    NSLog(message)
}