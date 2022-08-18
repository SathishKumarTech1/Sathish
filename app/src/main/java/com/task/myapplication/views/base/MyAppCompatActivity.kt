package com.task.myapplication.views.base

import android.app.Activity
import android.content.*
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.task.myapplication.R
import com.task.myapplication.enums.ErrorMessageType
import com.task.myapplication.enums.LoaderStatus
import com.task.myapplication.viewmodels.MyBaseViewModel


abstract class MyAppCompatActivity : AppCompatActivity() {
    protected val TAG = this.javaClass.simpleName
    protected val WAIT_TIME: Long = 1500

    private var mBaseView: ViewGroup? = null
    private var mLoaderView: View? = null
    private var progressShown = false

    private val viewModel: MyBaseViewModel by lazy {
        ViewModelProvider(this).get(MyBaseViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setUpLoader(viewModel)
        initProgress()
    }

    //Initializing the progress view
    private fun initProgress() {
        mBaseView = this.findViewById(android.R.id.content)
       // mLoaderView = View.inflate(this, R.layout.loader, null)
    }

    //To hide Keyboard
    fun hideKeyboard() {
        val inputMethodManager =
            this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = this.currentFocus
        view?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    //To show keyboard
    fun showKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        // check if no view has focus:
        val view = this.currentFocus
        view?.let { it ->
            inputManager.showSoftInput(it, InputMethodManager.SHOW_FORCED)
        }
    }

    //To show snackbar
    protected fun showSnackbar(
        message: String?,
        errorMessageType: ErrorMessageType = ErrorMessageType.snackbar
    ) {
        val snackbarMessage = SpannableStringBuilder(message)
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content), snackbarMessage,
            Snackbar.LENGTH_LONG
        )
        snackbar.duration = 3000
        val snackBarView = snackbar.view
        var snackbarBg = R.color.primaryVariant
        var snackbarTextColor = R.color.onError
        if (errorMessageType == ErrorMessageType.snackbarError) {
            snackbarBg = R.color.error
            snackbarTextColor = R.color.onError
        }
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, snackbarBg))
        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.maxLines = 4
        textView.setTextColor(ContextCompat.getColor(this, snackbarTextColor))
        snackbar.show()
    }

    //To show toast message
    protected fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private val defaultDialogClickListener = DialogInterface.OnClickListener { dialog, which ->
        dialog.dismiss()
    }

    //To show alert dialog with 'ok' button alone
    protected fun showAlertDialogOk(
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener = defaultDialogClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.ok), listener)
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.onPrimary))
        }

        mAlertDialog.show()
    }

    //To show alert dialog with Positive and Negative button with positive and negative button listener
    protected fun showConfirmation(
        negativeText: String,
        positiveText: String,
        title: String?,
        message: String,
        listener: DialogInterface.OnClickListener,
        negativeListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        if (title != null)
            builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(positiveText, listener)
        builder.setNegativeButton(negativeText, negativeListener)
        val mAlertDialog = builder.create()
        mAlertDialog.setCanceledOnTouchOutside(false)
        mAlertDialog.setCancelable(false)

        mAlertDialog.setOnShowListener {
            mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.secondary))
            mAlertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                .setTextColor(ContextCompat.getColor(this, R.color.green))
        }

        mAlertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //To show loader progress
    open fun showProgress() {
        hideKeyboard()
        if (!progressShown) {
            mBaseView!!.addView(mLoaderView)
            progressShown = true
        }
    }

    //To hide loader progress
    open fun hideProgress() {
        /*if (progressShown) {
            mBaseView!!.removeView(mLoaderView)
            progressShown = false
        }*/
    }

    //Call this method while setting up Viewmodel to init progress
    protected fun setUpLoader(viewModel: MyBaseViewModel) {
        viewModel.isLoading.observe(this, Observer {
            if (it.equals(LoaderStatus.loading))
               // showProgress()
            else
                hideProgress()
        })

        viewModel.errorLiveData.observe(this, Observer {
            it?.let {
                onErrorCalled(it)
            }
        })
     initObservers()

    }

    abstract fun initObservers()

    protected abstract fun onErrorCalled(it: String?)

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}