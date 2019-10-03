/*
 * Project: mybudget2-mobile-android
 * File: ActivationViewModel.kt
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

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.italiancoders.mybudget.manager.registrationuserinfo.RegistrationUserInfoManager
import it.italiancoders.mybudget.utils.ioJob
import it.italiancoders.mybudget.utils.uiJob
import javax.inject.Inject

/**
 * @author fattazzo
 *         <p/>
 *         date: 03/10/19
 */
class ActivationViewModel @Inject constructor(private val registrationUserInfoManager: RegistrationUserInfoManager) :
    ViewModel() {

    val username = MutableLiveData<String>()
    val codice = MutableLiveData<String>()

    val dataValid = MediatorLiveData<Boolean>().apply {
        addSource(codice) { postValue(isDataValid()) }
        addSource(username) { postValue(isDataValid()) }
    }

    private fun isDataValid(): Boolean =
        codice.value?.length == 5 && username.value.orEmpty().isNotBlank()

    fun confirmRegistration(onSuccessAction: () -> Unit, onFailureAction: () -> Unit) {
        if (dataValid.value == true) {

            ioJob {
                val confirmResult =
                    registrationUserInfoManager.confirm(username.value!!, codice.value!!)

                if (confirmResult == true) {
                    uiJob { onSuccessAction.invoke() }
                }
                if (confirmResult == false) {
                    uiJob { onFailureAction.invoke() }
                }
            }
        }
    }
}