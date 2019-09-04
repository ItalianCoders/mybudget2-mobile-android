/*
 * Project: mybudget2-mobile-android
 * File: RegistrationUserInfoViewModelTest.kt
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

import com.nhaarman.mockitokotlin2.doReturn
import it.italiancoders.mybudget.AbstractViewModelTest
import it.italiancoders.mybudget.manager.registrationuserinfo.RegistrationUserInfoManager
import it.italiancoders.mybudget.rest.models.UserRegistrationInfo
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/09/19
 */
class RegistrationUserInfoViewModelTest : AbstractViewModelTest<RegistrationUserInfoViewModel>() {

    @Mock
    lateinit var registrationUserInfoManager: RegistrationUserInfoManager

    override fun createViewModel(): RegistrationUserInfoViewModel =
        RegistrationUserInfoViewModel(registrationUserInfoManager)

    @Test
    override fun initialValues() {

        assertThat(viewModel.firstname.value, `is`(""))
        assertThat(viewModel.lastname.value, `is`(""))
        assertThat(viewModel.username.value, `is`(""))
        assertThat(viewModel.password.value, `is`(""))
        assertThat(viewModel.passwordConfirm.value, `is`(""))
        assertThat(viewModel.email.value, `is`(""))

        assertThat(viewModel.dataValid.value, `is`(false))
    }

    @Test
    fun dataValidation() {

        assertThat(viewModel.dataValid.value, `is`(false))

        // Ok data
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("testUser")
        viewModel.password.postValue("testUser1")
        viewModel.passwordConfirm.postValue("testUser1")
        viewModel.email.postValue("test@user.com")
        assertThat(viewModel.dataValid.value, `is`(true))

        // Username empty
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("")
        viewModel.password.postValue("testUser1")
        viewModel.passwordConfirm.postValue("testUser1")
        viewModel.email.postValue("test@user.com")
        assertThat(viewModel.dataValid.value, `is`(false))

        // Username to short
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("test")
        viewModel.password.postValue("testUser1")
        viewModel.passwordConfirm.postValue("testUser1")
        viewModel.email.postValue("test@user.com")
        assertThat(viewModel.dataValid.value, `is`(false))

        // Password empty
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("testUser")
        viewModel.password.postValue("")
        viewModel.passwordConfirm.postValue("testUser1")
        viewModel.email.postValue("test@user.com")
        assertThat(viewModel.dataValid.value, `is`(false))

        // Wrong password
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("testUser")
        viewModel.password.postValue("testuser")
        viewModel.passwordConfirm.postValue("testUser1")
        viewModel.email.postValue("test@user.com")
        assertThat(viewModel.dataValid.value, `is`(false))

        // Password != confirm password
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("testUser")
        viewModel.password.postValue("testUser1")
        viewModel.passwordConfirm.postValue("testUser2")
        viewModel.email.postValue("test@user.com")
        assertThat(viewModel.dataValid.value, `is`(false))

        // Email empty
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("testUser")
        viewModel.password.postValue("testUser1")
        viewModel.passwordConfirm.postValue("testUser1")
        viewModel.email.postValue("")
        assertThat(viewModel.dataValid.value, `is`(false))

        // Wrong email
        viewModel.firstname.postValue("testUser")
        viewModel.lastname.postValue("testUser")
        viewModel.username.postValue("testUser")
        viewModel.password.postValue("testUser1")
        viewModel.passwordConfirm.postValue("testUser1")
        viewModel.email.postValue("test@user")
        assertThat(viewModel.dataValid.value, `is`(false))
    }

    @Test
    fun createRegistration() {

        val userInfoOk = UserRegistrationInfo(
            "testUserOk",
            "testUserOk1",
            "testUserOk",
            "testUserOk",
            "test@user.com"
        )
        val userInfoNo = UserRegistrationInfo(
            "testUserNo",
            "testUserNo1",
            "testUserNo",
            "testUserNo",
            "test@user.com"
        )
        val userInfoNoNetwork = UserRegistrationInfo(
            "testUserNoNetwork",
            "testUserNoNetwork1",
            "testUserNoNetwork",
            "testUserNoNetwork",
            "test@user.com"
        )

        `when`(registrationUserInfoManager.create(userInfoOk)).doReturn(true)
        `when`(registrationUserInfoManager.create(userInfoNo)).doReturn(false)
        `when`(registrationUserInfoManager.create(userInfoNoNetwork)).doReturn(null)

        var passed = true
        setInfoToViewModel(userInfoOk)
        viewModel.createRegistration({ passed = true }, { passed = false })
        assertThat("User registration failed with valid info", passed)

        passed = true
        setInfoToViewModel(userInfoNo)
        viewModel.createRegistration({ passed = false }, { passed = true })
        assertThat("User registration success with invalid info", passed)

        passed = true
        setInfoToViewModel(userInfoNoNetwork)
        viewModel.createRegistration({ passed = false }, { passed = false })
        assertThat("User registration success or failed with no network available", passed)
    }

    private fun setInfoToViewModel(userInfo: UserRegistrationInfo) {
        viewModel.firstname.postValue(userInfo.firstName)
        viewModel.lastname.postValue(userInfo.lastName)
        viewModel.username.postValue(userInfo.username)
        viewModel.password.postValue(userInfo.password)
        viewModel.passwordConfirm.postValue(userInfo.password)
        viewModel.email.postValue(userInfo.email)
    }
}