/*
 * Project: mybudget2-mobile-android
 * File: AppComponent.kt
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

package it.italiancoders.mybudget.app.component

import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Preconditions
import dagger.Component
import it.italiancoders.mybudget.activity.categories.CategoriesActivity
import it.italiancoders.mybudget.activity.login.LoginActivity
import it.italiancoders.mybudget.activity.main.MainActivity
import it.italiancoders.mybudget.activity.movements.MovementsActivity
import it.italiancoders.mybudget.activity.movements.edit.MovementActivity
import it.italiancoders.mybudget.activity.movements.list.ListMovementsFragment
import it.italiancoders.mybudget.activity.movements.search.SearchMovementsView
import it.italiancoders.mybudget.activity.registration.RegistrationUserInfoActivity
import it.italiancoders.mybudget.app.module.AppModule
import it.italiancoders.mybudget.app.module.CategoriesModule
import it.italiancoders.mybudget.app.module.MovementsModule
import it.italiancoders.mybudget.app.module.SessionModule
import it.italiancoders.mybudget.rest.api.RefreshTokenInterceptor
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        CategoriesModule::class,
        MovementsModule::class,
        SessionModule::class]
)
interface AppComponent {

    object Singleton {
        private var sInstance: AppComponent? = null

        fun initialze(component: AppComponent) {
            sInstance = component
        }

        fun getsInstance(): AppComponent {
            return Preconditions.checkNotNull(sInstance)
        }
    }

    fun inject(activity: AppCompatActivity)
    fun inject(activity: MainActivity)
    fun inject(activity: CategoriesActivity)
    fun inject(activity: MovementsActivity)
    fun inject(activity: MovementActivity)
    fun inject(activity: LoginActivity)
    fun inject(activity: RegistrationUserInfoActivity)

    fun inject(view: SearchMovementsView)

    fun inject(fragment: ListMovementsFragment)

    fun inject(interceptor: RefreshTokenInterceptor)
}