/*
 * Project: mybudget2-mobile-android
 * File: AbstractRestManager.kt
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

package it.italiancoders.mybudget.manager

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import retrofit2.Call
import retrofit2.Response


/**
 * @author fattazzo
 *         <p/>
 *         date: 22/07/19
 */
abstract class AbstractRestManager(protected val context: Context) {

    protected fun <T> processResponse(
        response: Response<T>,
        onSuccessAction: ((T?) -> Unit)?,
        onFailureAction: ((Int?) -> Unit?)?,
        showErrorMessage: Boolean = true
    ) {
        try {
            if (response.isSuccessful) {
                onSuccessAction?.invoke(response.body())
            } else {
                if (showErrorMessage)
                    showError(response.message())
                onFailureAction?.invoke(response.code())
            }
        } catch (e: Exception) {
            if (showErrorMessage)
                showError()
            onFailureAction?.invoke(null)
        }
    }

    protected fun <T> processResponse(call: Call<T>, showErrorMessage: Boolean = true): T? {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                response.body()
            } else {
                if (showErrorMessage)
                    showError(response.message())
                null
            }
        } catch (e: Exception) {
            if (showErrorMessage)
                showError()
            null
        }
    }

    protected fun <T> processResponseWithResult(
        call: Call<T>,
        showErrorMessage: Boolean = true
    ): Result<T> {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                Result(response.body(), null, null)
            } else {
                if (showErrorMessage)
                    showError(response.message())
                Result(null, response.code(), null)
            }
        } catch (e: Exception) {
            if (showErrorMessage)
                showError()
            Result(null, null, e)
        }
    }

    protected fun processVoidResponse(call: Call<Void>, showErrorMessage: Boolean = true): Boolean {
        return try {
            val response = call.execute()
            if (response.isSuccessful) {
                true
            } else {
                if (showErrorMessage)
                    showError(response.message())
                false
            }
        } catch (e: Exception) {
            if (showErrorMessage)
                showError()
            false
        }
    }

    private fun showError(message: String = "Errore!") {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}