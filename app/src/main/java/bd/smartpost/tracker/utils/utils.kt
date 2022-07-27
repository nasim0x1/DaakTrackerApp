package bd.smartpost.tracker.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import bd.smartpost.tracker.R
import bd.smartpost.tracker.ui.view.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*


enum class ParcelTypes{
    Domestic,International
}

enum class ParcelState{
    Loading,Updated,Failed
}

fun getCurrentDateTime(): String = SimpleDateFormat("h:mm a, MMM d yyyy").format(Date())

fun Context.isInternetAvailable() =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
        getNetworkCapabilities(activeNetwork)?.run {
            hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } ?: false
    }



inline fun Fragment.myAlertDialog(
    message: String,
    positiveButtonText:String,
    negativeButtonText:String,
    crossinline callback:() -> Unit) {
    val dialog = android.app.AlertDialog.Builder(requireContext())
    dialog.setMessage(message)
    dialog.setPositiveButton(positiveButtonText) { view, _ ->
        callback()
        view.dismiss()
    }
    dialog.setNegativeButton(negativeButtonText){view,_->view.dismiss()}
    dialog.show()
}

fun Fragment.showSnakeMessage(msg: String, above: Boolean = true) {
    val bbView =
        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    val snack =
        Snackbar.make(activity!!.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG)
    if (above) {
        snack.anchorView = bbView
    }
    snack.show();
}