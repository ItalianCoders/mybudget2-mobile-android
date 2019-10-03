/*
 * Project: mybudget2-mobile-android
 * File: ActivationActivity.kt
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

package it.italiancoders.mybudget.activity.activation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.activity.login.LoginActivity
import it.italiancoders.mybudget.app.MyBudgetApplication
import it.italiancoders.mybudget.app.module.viewModel.DaggerViewModelFactory
import it.italiancoders.mybudget.databinding.ActivityActivationBinding
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 03/10/19
 */
class ActivationActivity : BaseActivity<ActivityActivationBinding>() {

    companion object {
        const val USERNAME_EXTRA = "username_extra"
    }

    @Inject
    lateinit var viewModelFactory: DaggerViewModelFactory

    lateinit var viewModel: ActivationViewModel

    override fun getLayoutResID(): Int = R.layout.activity_activation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as MyBudgetApplication).appComponent.inject(this)

        viewModel = ViewModelProvider(this, viewModelFactory).get(ActivationViewModel::class.java)
        binding.model = viewModel

        if (intent.extras?.containsKey(USERNAME_EXTRA) == true) {
            viewModel.username.postValue(intent.extras!!.getString(USERNAME_EXTRA, ""))
        }
    }

    override fun openLoginActivityOnNoSessionData(): Boolean = false

    fun confirm(view: View) {

        viewModel.confirmRegistration(
            {
                this.currentFocus?.let { v ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(v.windowToken, 0)
                }

                startActivity(Intent(this@ActivationActivity, LoginActivity::class.java))
                this@ActivationActivity.finish()
            },
            {
                Toast.makeText(this, R.string.confirm_error, Toast.LENGTH_SHORT).show()
            }
        )
    }
}