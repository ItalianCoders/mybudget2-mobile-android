/*
 * Project: mybudget2-mobile-android
 * File: LoginActivity.kt
 *
 * Created by fattazzo
 * Copyright © 2019 Gianluca Fattarsi. All rights reserved.
 *
 * MIT License
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package it.italiancoders.mybudget.activity.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.setActionButtonEnabled
import com.afollestad.materialdialogs.input.getInputField
import com.afollestad.materialdialogs.input.input
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.activity.registration.RegistrationUserInfoActivity
import it.italiancoders.mybudget.app.MyBudgetApplication
import it.italiancoders.mybudget.app.module.viewModel.DaggerViewModelFactory
import it.italiancoders.mybudget.databinding.ActivityLoginBinding
import it.italiancoders.mybudget.manager.registrationuserinfo.RegistrationUserInfoManager
import it.italiancoders.mybudget.utils.PrivacyPolicyManager
import javax.inject.Inject

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    companion object {

        const val REQUEST_CODE_LOGIN = 2000
    }

    @Inject
    lateinit var registrationUserInfoManager: RegistrationUserInfoManager

    @Inject
    lateinit var loginViewModelFactory: DaggerViewModelFactory

    private lateinit var loginViewModel: LoginViewModel

    override fun getLayoutResID(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyBudgetApplication).appComponent.inject(this)

        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        binding.viewModel = loginViewModel

        // Animate header and footer slide in
        LoginHeaderFooterAnimator.start(binding.headerContainer)
        LoginHeaderFooterAnimator.start(binding.footerContainer, false)
    }

    fun login(view: View) {

        loginViewModel.login(
            {
                setResult(Activity.RESULT_OK, Intent())
                this@LoginActivity.finish()
            },
            {
                if (it == 403) {
                    MaterialDialog(this).show {
                        title(R.string.login_user_not_activated_title)
                        message(R.string.login_user_not_activated_message)
                        icon(R.drawable.ic_error)
                        positiveButton { this@LoginActivity.recreate() }
                    }
                } else {
                    Toast.makeText(this, R.string.login_error, Toast.LENGTH_SHORT).show()
                    this@LoginActivity.recreate()
                }
            }
        )

        if (loginViewModel.dataValid.value == true) animateLoginButton()
    }

    fun showPrivacyPolicy(view: View) {
        PrivacyPolicyManager.showContent(this)
    }

    fun openRegistrationActicity(view: View) {
        val intent = Intent(this.applicationContext, RegistrationUserInfoActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun resendRegistration(view: View) {

        MaterialDialog(this).show {
            input(
                inputType = InputType.TYPE_CLASS_TEXT,
                waitForPositiveButton = false
            ) { dialog, text ->
                val inputField = dialog.getInputField()
                val valid = UserValidationRules.USERNAME.isValid(inputField.editableText)

                inputField.error =
                    if (valid) null else inputField.context.getString(R.string.error_username_invalid)
                dialog.setActionButtonEnabled(WhichButton.POSITIVE, valid)
            }
            title(R.string.login_username_hint)
            positiveButton(R.string.submit) {
                registrationUserInfoManager.resend(
                    it.getInputField().editableText.toString(),
                    {
                        Toast.makeText(
                            this@LoginActivity,
                            "Inviato correttamente",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
            negativeButton(android.R.string.cancel)
        }
    }

    private fun animateLoginButton() {

        // Width animation from button width to R.dimen.sign_button_loading_width in 250 millisec.
        val finalWidth = resources.getDimension(R.dimen.sign_button_loading_width).toInt()
        val anim = ValueAnimator.ofInt(binding.signInButton.measuredWidth, finalWidth)
        anim.addUpdateListener {
            val value: Int = it.animatedValue as Int
            val layoutParam = binding.signInButton.layoutParams
            layoutParam.width = value
            binding.signInButton.requestLayout()
        }
        anim.duration = 250
        anim.start()

        // Login text animation from alpha 1 to 0 and progress bar visibility at the end
        binding.signInText.animate().alpha(0f).setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
            ).start()
    }

    override fun checkUserSession(): Boolean = false

    override fun onBackPressed() {
        when {
            binding.usernameET.isFocused -> binding.usernameET.clearFocus()
            binding.passwordET.isFocused -> binding.passwordET.clearFocus()
            else -> super.onBackPressed()
        }
    }
}
