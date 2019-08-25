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

import android.text.Editable
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {

    val username = MutableLiveData<String>().apply { postValue("") }
    val password = MutableLiveData<String>().apply { postValue("") }
    val policyAccepted = MutableLiveData<Boolean>().apply { false }

    val dataValid = MutableLiveData<Boolean>().apply { postValue(false) }

    private val dataMediator = MediatorLiveData<String>().apply {
        addSource(username) { value ->
            setValue(value)
            dataValid.value = checkDataValid()
        }
        addSource(password) { value ->
            setValue(value)
            dataValid.value = checkDataValid()
        }
    }.also { it.observeForever { /* empty */ } }

    private val booleanDataMediator = MediatorLiveData<Boolean>().apply {
        addSource(policyAccepted) { value ->
            setValue(value)
            dataValid.value = checkDataValid()
        }
    }.also { it.observeForever { /* empty */ } }

    private fun checkDataValid(): Boolean {
        val usernameValid =
            UserValidationRules.USERNAME.isValid(Editable.Factory.getInstance().newEditable(username.value.orEmpty()))
        val passwordValid =
            UserValidationRules.PASSWORD.isValid(Editable.Factory.getInstance().newEditable(password.value.orEmpty()))
        return usernameValid && passwordValid && policyAccepted.value?:false
    }
}


