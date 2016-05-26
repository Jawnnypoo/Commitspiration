package com.commit451.commitspiration.extensions

import android.support.design.widget.Snackbar
import android.view.View

/**
 * View extensions
 */
fun View.snack(message: String, length: Int = Snackbar.LENGTH_SHORT) {
    val snack = Snackbar.make(this, message, length)
    snack.show()
}
