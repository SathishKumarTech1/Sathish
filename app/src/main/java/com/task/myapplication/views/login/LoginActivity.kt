package com.task.myapplication.views.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider

import com.task.myapplication.views.base.MyAppCompatActivity
import com.task.myapplication.R
import com.task.myapplication.databinding.ActivityLoginBinding
import com.task.myapplication.viewmodels.launch.AuthViewModel
import com.task.myapplication.views.MainActivity

class LoginActivity : MyAppCompatActivity() {
    private var binding: ActivityLoginBinding? = null

    private val viewModel: AuthViewModel by lazy {
        ViewModelProvider(this).get(AuthViewModel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Databinding with layouts
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setUpLoader(viewModel)
        initViews()
    }

    override fun initObservers() {
        viewModel.loginLiveData.observe(this) { message ->
            message?.let {
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }

    }



    override fun onErrorCalled(it: String?) {
        it?.let { error ->
             showSnackbar(error)

        }
    }

    private fun initViews() {
        binding?.buttonLogin?.isEnabled = false
        setupListeners()

        binding?.buttonLogin?.setOnClickListener {

            hideKeyboard()

            var userEmailId: String = binding?.loginPersonName?.text.toString()
            var password: String = binding?.LoginPassword?.text.toString()
            viewModel.login(userEmailId,password)
        }
    }


    private fun isValidate(): Boolean {
        val validData = validateEmail() && validatePassword()
        if (validData) {
            binding?.buttonLogin?.isEnabled = true
            binding?.buttonLogin?.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.secondary
                )
            )

        } else {
            binding?.buttonLogin?.isEnabled = false
            binding?.buttonLogin?.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.secondary_Enabled_low
                )
            )

        }
        return true
    }


    private fun setupListeners() {
        binding?.loginPersonName?.addTextChangedListener(TextFieldValidation(binding?.loginPersonName!!))
        binding?.LoginPassword?.addTextChangedListener(TextFieldValidation(binding?.LoginPassword!!))

    }


    private fun validateEmail(): Boolean {
        if (binding?.loginPersonName?.text.toString().trim().isEmpty()) {
            showSnackbar(getString(R.string.enter_your_email_login))
            return false
        } else {

        }
        return true
    }


    private fun validatePassword(): Boolean {
        if (binding?.LoginPassword?.text.toString().trim().isEmpty()) {
            showSnackbar(getString(R.string.enter_your_password_login))
            return false
        } else if (binding?.LoginPassword?.text.toString().length < 8) {
            showSnackbar(getString(R.string.password_must_be_at_least_eight_characters))
            return false
        }  else {
        }
        return true
    }

    // applying text watcher on each text field

    inner class TextFieldValidation(private val view: View) : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // checking ids of each text field and applying functions accordingly.
            when (view.id) {
                R.id.loginPersonName -> {
                    validateEmail()
                }

                R.id.LoginPassword -> {
                    validatePassword()
                }

            }
            isValidate()
        }
    }
}