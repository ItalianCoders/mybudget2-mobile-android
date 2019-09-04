/*
 * Project: mybudget2-mobile-android
 * File: RegistrationUserInfoManager.kt
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

package it.italiancoders.mybudget.manager.registrationuserinfo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.manager.AbstractRestManager
import it.italiancoders.mybudget.rest.api.RetrofitBuilder
import it.italiancoders.mybudget.rest.api.services.RegistrationUserInfoRestService
import it.italiancoders.mybudget.rest.models.UserRegistrationInfo
import it.italiancoders.mybudget.utils.NetworkChecker
import it.italiancoders.mybudget.utils.OpenForTesting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/08/19
 */
@OpenForTesting
class RegistrationUserInfoManager(context: Context) : AbstractRestManager(context) {

    private val registrationUserInfoRestService =
        RetrofitBuilder.client.create(RegistrationUserInfoRestService::class.java)

    fun create(userRegistrationInfo: UserRegistrationInfo) : Boolean? {

        if (!NetworkChecker().isNetworkAvailable(context)) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(
                    context,
                    R.string.network_unavailable_dialog_title,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            return null
        }

        val response = registrationUserInfoRestService.create(userRegistrationInfo)

        return processResponse(response,false) != null
    }

    fun resend(
        userName: String,
        onSuccessAction: (Void?) -> Unit,
        onFailureAction: ((Int?) -> Unit)? = null
    ) {
        if (!NetworkChecker().isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.network_unavailable_dialog_title, Toast.LENGTH_SHORT)
                .show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = registrationUserInfoRestService.resend(userName)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }

    fun confirm(
        token: String,
        onSuccessAction: (Void?) -> Unit,
        onFailureAction: (Int?) -> Unit
    ) {
        if (!NetworkChecker().isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.network_unavailable_dialog_title, Toast.LENGTH_SHORT)
                .show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = registrationUserInfoRestService.confirm(token)
            withContext(Dispatchers.Main) {
                processResponse(response, onSuccessAction, onFailureAction)
            }
        }
    }
}