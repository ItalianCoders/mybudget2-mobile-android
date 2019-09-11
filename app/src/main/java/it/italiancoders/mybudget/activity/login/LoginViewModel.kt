/*
 * Project: mybudget2-mobile-android
 * File: LoginViewModel.kt
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

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.session.SessionManager
import it.italiancoders.mybudget.utils.ioJob
import it.italiancoders.mybudget.utils.uiJob
import java.util.*


class LoginViewModel(private val sessionManager: SessionManager) : ViewModel() {

    val username = MutableLiveData<String>().apply { postValue("") }
    val password = MutableLiveData<String>().apply { postValue("") }
    val policyAccepted = MutableLiveData<Boolean>().apply { postValue(false) }

    val dataValid = MediatorLiveData<Boolean>().apply {
        value = false
        addSource(username) { setValue(checkDataValid()) }
        addSource(password) { setValue(checkDataValid()) }
        addSource(policyAccepted) { setValue(checkDataValid()) }
    }.also { it.observeForever { /* empty */ } }

    private fun checkDataValid(): Boolean {
        val usernameValid = UserValidationRules.REQUIRED.isValid(username.value)
        val passwordValid = UserValidationRules.REQUIRED.isValid(password.value)
        return usernameValid && passwordValid && (policyAccepted.value ?: false)
    }

    fun login(onSuccessAction: () -> Unit, onFailureAction: (Int) -> Unit) {
        if(dataValid.value != true) return

        ioJob {

            val result = sessionManager.login(username.value!!,password.value!!, Locale.getDefault().language)

            uiJob {

                if(result.session != null) {
                    onSuccessAction.invoke()
                } else {
                    onFailureAction.invoke(result.errorCode ?: 400)
                }
            }
        }
    }
}


