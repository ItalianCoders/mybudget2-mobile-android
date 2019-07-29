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
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.databinding.ActivityLoginBinding
import it.italiancoders.mybudget.manager.session.SessionManager
import it.italiancoders.mybudget.rest.models.Session
import it.italiancoders.mybudget.utils.PrivacyPolicyManager
import java.util.*


class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    override fun getLayoutResID(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setSupportActionBar(binding.toolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        //supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        // Animate header and footer slide in
        LoginHeaderFooterAnimator.start(binding.headerContainer)
        LoginHeaderFooterAnimator.start(binding.footerContainer, false)
    }

    private fun isLoginDataValid(): Boolean {
        val usernameValid = binding.usernameLayout.editText?.error == null
        val passwordValid = binding.passwordLayout.editText?.error == null
        return usernameValid && passwordValid
    }

    fun login(view: View) {
        if (isLoginDataValid()) {
            animateLoginButton()

            val username = binding.usernameET.text.toString()
            val password = binding.passwordET.text.toString()
            val locale = Locale.getDefault().language
            val successAction: (Session?) -> Unit = { this@LoginActivity.finish() }
            val failureAction = { this@LoginActivity.recreate() }

            SessionManager(this)
                .login(username, password, locale, successAction, failureAction)
        }
    }

    fun showPrivacyPolicy(view: View) {
        PrivacyPolicyManager.showContent(this)
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
        binding.signInText.animate().alpha(0f).setDuration(250).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
        ).start()
    }

    override fun onBackPressed() {
        when {
            binding.usernameET.isFocused -> binding.usernameET.clearFocus()
            binding.passwordET.isFocused -> binding.passwordET.clearFocus()
            else -> super.onBackPressed()
        }
    }
}
