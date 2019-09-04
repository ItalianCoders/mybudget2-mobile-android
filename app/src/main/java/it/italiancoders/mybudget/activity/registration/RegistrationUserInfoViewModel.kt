/*
 * Project: mybudget2-mobile-android
 * File: RegistrationUserInfoViewModel.kt
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

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.activity.login.UserValidationRules
import it.italiancoders.mybudget.manager.registrationuserinfo.RegistrationUserInfoManager
import it.italiancoders.mybudget.rest.models.UserRegistrationInfo
import it.italiancoders.mybudget.utils.ioJob

/**
 * @author fattazzo
 *         <p/>
 *         date: 24/08/19
 */
class RegistrationUserInfoViewModel(private val registrationUserInfoManager: RegistrationUserInfoManager) :
    ViewModel() {

    val firstname = MutableLiveData<String>("")
    val lastname = MutableLiveData<String>("")
    val username = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")
    val passwordConfirm = MutableLiveData<String>("")
    val email = MutableLiveData<String>("")

    val dataValid = MediatorLiveData<Boolean>().apply {
        value = false
        addSource(firstname) { value = checkDataValid() }
        addSource(lastname) { value = checkDataValid() }
        addSource(username) { value = checkDataValid() }
        addSource(password) { value = checkDataValid() }
        addSource(passwordConfirm) { value = checkDataValid() }
        addSource(email) { value = checkDataValid() }
    }.also { it.observeForever { /* empty */ } }

    private fun checkDataValid(): Boolean {
        val firstnameValid =
            UserValidationRules.FIRSTNAME.isValid(firstname.value.orEmpty())
        val lastnameValid =
            UserValidationRules.LASTNAME.isValid(lastname.value.orEmpty())
        val usernameValid =
            UserValidationRules.USERNAME.isValid(username.value.orEmpty())
        val passwordValid =
            UserValidationRules.PASSWORD.isValid(password.value.orEmpty())
        val passwordConfirmValid =
            UserValidationRules.PASSWORD.isValid(passwordConfirm.value.orEmpty())
        val emailValid =
            UserValidationRules.EMAIL.isValid(email.value.orEmpty())

        return firstnameValid && lastnameValid && usernameValid && passwordValid && passwordConfirmValid && emailValid && password.value.orEmpty() == passwordConfirm.value
    }

    fun createRegistration(onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
        if (dataValid.value == true) {

            val userRegistrationInfo = UserRegistrationInfo(
                username.value!!,
                password.value!!,
                firstname.value!!,
                lastname.value!!,
                email.value!!
            )

            ioJob {
                val createResult = registrationUserInfoManager.create(userRegistrationInfo)

                if (createResult == true) {
                    onSuccessAction.invoke()
                }
                if (createResult == false) {
                    onFailureAction.invoke()
                }
            }
        }
    }
}