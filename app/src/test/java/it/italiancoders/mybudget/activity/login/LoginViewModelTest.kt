/*
 * Project: mybudget2-mobile-android
 * File: LoginViewModelTest.kt
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

import it.italiancoders.mybudget.AbstractViewModelTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

/**
 * @author fattazzo
 *         <p/>
 *         date: 29/08/19
 */
class LoginViewModelTest : AbstractViewModelTest<LoginViewModel>() {

    override fun createViewModel(): LoginViewModel = LoginViewModel()

    @Test
    override fun initialValues() {
        assertThat(viewModel.username.value, `is`(""))
        assertThat(viewModel.password.value, `is`(""))

        assertThat(viewModel.policyAccepted.value, `is`(false))

        assertThat(viewModel.dataValid.value, `is`(false))
    }

    @Test
    fun validateUsername() {

        var valid = UserValidationRules.REQUIRED.isValid(viewModel.username.value)
        assertThat(valid, `is`(false))

        viewModel.username.postValue("  ")
        valid = UserValidationRules.REQUIRED.isValid(viewModel.username.value)
        assertThat(valid, `is`(false))

        viewModel.username.postValue("testtest")
        valid = UserValidationRules.REQUIRED.isValid(viewModel.username.value)
        assertThat(valid, `is`(true))
    }

    @Test
    fun validatePassword() {

        var valid = UserValidationRules.REQUIRED.isValid(viewModel.password.value)
        assertThat(valid, `is`(false))

        viewModel.password.postValue(" ")
        valid = UserValidationRules.REQUIRED.isValid(viewModel.password.value)
        assertThat(valid, `is`(false))

        viewModel.password.postValue("Pwdpwdpwdpwd1")
        valid = UserValidationRules.REQUIRED.isValid(viewModel.password.value)
        assertThat(valid, `is`(true))
    }

    @Test
    fun validateLoginData() {

        viewModel.username.postValue("testtest")
        assertThat(UserValidationRules.REQUIRED.isValid(viewModel.username.value), `is`(true))
        assertThat(viewModel.dataValid.value, `is`(false))

        viewModel.password.postValue("Testtest1")
        assertThat(UserValidationRules.REQUIRED.isValid(viewModel.password.value), `is`(true))
        assertThat(viewModel.dataValid.value, `is`(false))

        viewModel.policyAccepted.postValue(true)
        assertThat(viewModel.policyAccepted.value, `is`(true))
        assertThat(viewModel.dataValid.value, `is`(true))
    }
}