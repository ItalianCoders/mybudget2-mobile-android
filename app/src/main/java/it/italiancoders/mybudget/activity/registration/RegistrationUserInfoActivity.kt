/*
 * Project: mybudget2-mobile-android
 * File: RegistrationUserInfoActivity.kt
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

package it.italiancoders.mybudget.activity.registration

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.app.component.AppComponent
import it.italiancoders.mybudget.databinding.ActivityRegistrationUserInfoBinding
import it.italiancoders.mybudget.manager.registrationuserinfo.RegistrationUserInfoManager
import javax.inject.Inject

class RegistrationUserInfoActivity : BaseActivity<ActivityRegistrationUserInfoBinding>() {

    @Inject
    lateinit var registrationUserInfoManager: RegistrationUserInfoManager

    override fun getLayoutResID(): Int = R.layout.activity_registration_user_info

    override fun checkUserSession(): Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = ViewModelProvider(
            this,
            RegistrationUserInfoViewModelFactory(registrationUserInfoManager)
        ).get(RegistrationUserInfoViewModel::class.java)
    }

    override fun injectComponent(appComponent: AppComponent) {
        appComponent.inject(this)
    }

    fun createReagistration(view: View) {
        if (binding.model?.dataValid?.value == true) {
            animateLoginButton()
        }
        binding.model?.createRegistration(
            { showSuccessfullyRegistrationCreatedMessage() },
            { showFailureRegistrationCreatedMessage() }
        )
    }

    private fun showSuccessfullyRegistrationCreatedMessage() {
        MaterialDialog(this).show {
            title(R.string.registration_created_title)
            message(R.string.registration_created_message)
            icon(android.R.drawable.ic_dialog_email)
            positiveButton { this@RegistrationUserInfoActivity.finish() }
        }
    }

    private fun showFailureRegistrationCreatedMessage() {
        MaterialDialog(this).show {
            title(R.string.registration_created_failure_title)
            message(R.string.registration_created_failure_message)
            icon(R.drawable.ic_error)
            positiveButton { }
        }
    }

    private fun animateLoginButton() {

        // Width animation from button width to R.dimen.sign_button_loading_width in 250 millisec.
        val finalWidth = resources.getDimension(R.dimen.sign_button_loading_width).toInt()
        val anim = ValueAnimator.ofInt(binding.signUpButton.measuredWidth, finalWidth)
        anim.addUpdateListener {
            val value: Int = it.animatedValue as Int
            val layoutParam = binding.signUpButton.layoutParams
            layoutParam.width = value
            binding.signUpButton.requestLayout()
        }
        anim.duration = 250
        anim.start()

        // Login text animation from alpha 1 to 0 and progress bar visibility at the end
        binding.signUpText.animate().alpha(0f).setDuration(250)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
            ).start()
    }

    override fun onBackPressed() {
        when {
            binding.firstnameET.isFocused -> binding.firstnameET.clearFocus()
            binding.lastnameET.isFocused -> binding.lastnameET.clearFocus()
            binding.usernameET.isFocused -> binding.usernameET.clearFocus()
            binding.passwordET.isFocused -> binding.passwordET.clearFocus()
            binding.emailET.isFocused -> binding.emailET.clearFocus()
            else -> super.onBackPressed()
        }
    }
}
