package app.qup.ui.common

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import com.google.android.material.snackbar.Snackbar

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.snack(message: String, duration: Int = Snackbar.LENGTH_LONG, gravity: Int = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL) {

    try {
        val snack: Snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        val view = snack.view

        val snackbarView: View = snack.view
        val textView: TextView = snackbarView.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        textView.maxLines = 5

        // snackbarView.setBackgroundColor()

        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = gravity
        view.layoutParams = params
        snack.show()
    } catch (e: Exception) {e.printStackTrace()}
}

fun DialogFragment.setWidthPercent(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
}

fun DialogFragment.setWidthPercentFullHeight(percentage: Int) {
    val percent = percentage.toFloat() / 100
    val dm = Resources.getSystem().displayMetrics
    val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
    val percentWidth = rect.width() * percent
    dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.MATCH_PARENT)
}