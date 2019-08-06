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
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @author fattazzo
 *         <p/>
 *         date: 22/07/19
 */
abstract class AbstractRestManager(protected val context: Context) {

    protected fun <T> enqueueRequest(call: Call<T>, onSuccessAction: (T?) -> Unit, onFailureAction: () -> Unit) {
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {

                if (response.isSuccessful) {
                    onSuccessAction.invoke(response.body())
                } else {
                    Toast.makeText(context, "ERRORE! ${response.message()}", Toast.LENGTH_SHORT).show()
                    onFailureAction.invoke()
                }
            }

            override fun onFailure(call: Call<T>?, t: Throwable?) {
                Toast.makeText(context, "ERRORE!!", Toast.LENGTH_SHORT).show()
                onFailureAction.invoke()
            }
        })
    }

    protected fun <T> processResponse(
        response: Response<T>,
        onSuccessAction: ((T?) -> Unit)?,
        onFailureAction: (() -> Unit?)?
    ) {
        try {
            if (response.isSuccessful) {
                onSuccessAction?.invoke(response.body())
            } else {
                Toast.makeText(context, "ERRORE! ${response.message()}", Toast.LENGTH_SHORT).show()
                onFailureAction?.invoke()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "ERRORE!!", Toast.LENGTH_SHORT).show()
            onFailureAction?.invoke()
        }

    }
}