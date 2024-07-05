package com.okebenoithub.android.www.utils

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * MainUtil class
 * Created by: Presly Ben
 */
class MainUtil @Inject constructor() {
    private var uniqueID: String? = null

    /**
     * Detect Night mode
     * @param context :: context
     */
    private fun detectNightMode(context: Context): Boolean {
        return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            Configuration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }

    /**
     * Show toast message
     * @param context :: context
     * @param message :: message to show
     */
    fun showToastMessage(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        // set the gravity
        toast.setGravity(0, 0, 0)
        toast.show() // display the toast
    }

    /**
     * Set bottom bar navigation background color
     * @param window :: window
     * @param context :: context
     * @param defaultSystemBgColor :: default system background color
     * @param darkModeSystemColor :: dark mode system background color
     */
    fun setBottomBarNavigationBackgroundColor(
        window: Window,
        context: Context,
        defaultSystemBgColor: Int,
        darkModeSystemColor: Int
    ) {
        window.navigationBarColor = ContextCompat.getColor(context, defaultSystemBgColor)
        if (detectNightMode(context)) {
            window.navigationBarColor = ContextCompat.getColor(
                context,
                darkModeSystemColor
            )
        }
    }

    /**
     * Set Action Bar background color
     * @param context :: context
     * @param actionBar :: action bar
     * @param bgColorRes :: color int from resources
     */
    fun setActionBarBackgroundColor(context: Context, actionBar: ActionBar?, bgColorRes: Int) {
        // Define ActionBar object;
        if (actionBar == null) return
        // Define ColorDrawable object and color res int
        // with color int res code as its parameter
        val colorDrawable = ColorDrawable(ContextCompat.getColor(context, bgColorRes))

        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable)
    }

    /**
     * Capitalize each word from string
     * @param stringInput :: the string to transform
     * @return new string with each word capitalized
     */
    fun capitalizeEachWordFromString(stringInput: String): String {
        val strArray = stringInput.lowercase(Locale.getDefault()).split(" ".toRegex()).toTypedArray()
        val builder = StringBuilder()
        for (s in strArray) {
            val cap = s.substring(0, 1).uppercase(Locale.getDefault()) + s.substring(1)
            builder.append(cap).append(" ")
        }
        return builder.toString()
    }

    /**
     * This method is used for checking valid email id format.
     * @param email to check for
     * @return boolean true for valid false for invalid
     */
    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    /**
     * Get random number from range
     * @param min :: minimum
     * @param max :: maximum
     */
    fun getRandomNumber(min: Int, max: Int): Int = (min..max).random()

    /**
     * Compose email intent
     * @param addresses :: address to send email to
     * @param subject :: email subject
     */
    fun composeEmail(
        context: Context,
        addresses: Array<String?>?,
        subject: String?,
        message: String?,
        sharerTitle: String?
    ) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/html"
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(intent, sharerTitle)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(chooser)
        }
    }

    /**
     * This method checks for a valid name :: contains only letters
     * @param name :: name to check
     * @return true or false
     */
    fun isValidName(name: String): Boolean {
        val nameRegX = Regex("^[\\p{L} .'-]+$")
        return name.matches(nameRegX)
    }

    /**
     * Get number of column width for Grid layout auto fit
     * @param context :: context
     * @param columnWidthDp :: column width in dp
     */
    fun getNumbColumnsForGridLayoutAutoFit(context: Context, columnWidthDp: Float): Int {
        // For example columnWidthDp=180
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    /**
     * Share Text data through app
     * @param shareAboutTitle :: title of share dialog
     * @param textToShare :: text data to share
     */
    fun shareTextData(activity: Activity, shareAboutTitle: String?, textToShare: String?) {
        val mimeType = "text/plain"
        // Use ShareCompat.IntentBuilder to build the Intent and start the chooser
        ShareCompat
            .IntentBuilder(activity)
            .setType(mimeType)
            .setChooserTitle(shareAboutTitle)
            .setText(textToShare)
            .startChooser()
    }

    /**
     * Open web page
     * url: page url to load or open
     * @param context :: context
     * @param url :: url to load
     */
    fun openWebPage(context: Context, url: String) {
        val webPage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webPage)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    /**
     * Display snack bar message
     * @param contextView :: coordinatorLayout root view
     * @param messageResId :: message resource id string
     * @param snackBarDuration :: snack bar duration
     * @return snack bar
     */
    fun displaySnackBarMessage(
        contextView: CoordinatorLayout?,
        messageResId: String,
        snackBarDuration: Int
    ): Snackbar {
        val snackBar = Snackbar.make(
            contextView!!,
            messageResId, snackBarDuration
        )
        snackBar.show()
        return snackBar
    }

    /**
     * Get unique android device id
     * @param context :: context
     * @return :: return unique id
     */
    @Synchronized
    fun getUniqueID(context: Context): String? {
        if (uniqueID == null) {
            val prefUniqueId = "PREF_UNIQUE_ID"
            val sharedPrefs = context.getSharedPreferences(
                prefUniqueId, Context.MODE_PRIVATE
            )
            uniqueID = sharedPrefs.getString(prefUniqueId, null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor = sharedPrefs.edit()
                editor.putString(prefUniqueId, uniqueID)
                editor.apply()
            }
        }
        return uniqueID
    }

    /**
     * Hide soft input keyboard
     * @param contextView :: view to hide keyboard
     * @param activity :: activity
     */
    fun hideSoftInputKeyboard(contextView: View, activity: Activity) {
        val inputMethodManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // on below line hiding our keyboard.
        inputMethodManager.hideSoftInputFromWindow(contextView.windowToken, 0)
    }

    /**
     * Replacement for Kotlin's deprecated `capitalize()` function.
     * @param dataString :: string to capitalize
     * @return capitalized string
     */
    fun capitalized(dataString: String): String {
        return dataString.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

    /**
     * Underline data string
     * @param dataString :: string to underline
     * @return underline string
     */
    fun underlineStr(dataString: String): SpannableString {
        val mSpannableString = SpannableString(dataString)
        mSpannableString.setSpan(UnderlineSpan(), 0, mSpannableString.length, 0)
        return mSpannableString
    }

    interface SetSpinnerListViewCallback {
        fun onSpinnerItemSelected(view: View?, position: Int)
    }

    /**
     * Set spinner items list
     * @param context :: context
     * @param rootView :: root view
     * @param spinnerResId :: spinner resource id
     * @param arrayItems :: array items
     * @param spinnerItemLayout :: spinner item layout
     * @param selectedItemPosition :: selected item position
     * @param setSpinnerListViewCallback :: callback
     */
    fun setSpinnerItemsList(context: Context, rootView: View, spinnerResId: Int, arrayItems: Array<String>, spinnerItemLayout: Int, selectedItemPosition: Int = 0, setSpinnerListViewCallback: SetSpinnerListViewCallback) {
        val nightModeFlags = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES

        val spinner: Spinner = rootView.findViewById(spinnerResId)
        val spinnerItemsList: Array<String> = arrayItems
        val spinnerItemsAdapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
            context, spinnerItemLayout, spinnerItemsList
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent)
                val tv = view as TextView
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY)
                } else {
                    tv.setTextColor(Color.BLACK)
                    if (isNightMode) tv.setTextColor(Color.WHITE)
                }
                return view
            }
        }

        spinnerItemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        spinner.adapter = spinnerItemsAdapter
        spinner.setSelection(selectedItemPosition)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                setSpinnerListViewCallback.onSpinnerItemSelected(view, position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    /**
     * Open pdf file
     * @param requireContext :: context
     * @param pdfFilePath :: pdf file path
     */
    fun openPdfFile(requireContext: Context, pdfFilePath: String, openPdfFileBoxTitle: String) {

    }
}