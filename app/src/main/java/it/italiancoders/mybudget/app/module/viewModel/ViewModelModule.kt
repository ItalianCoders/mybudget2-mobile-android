/*
 * Project: mybudget2-mobile-android
 * File: ViewModelModule.kt
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

package it.italiancoders.mybudget.app.module.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import it.italiancoders.mybudget.activity.activation.ActivationViewModel
import it.italiancoders.mybudget.activity.categories.CategoriesViewModel
import it.italiancoders.mybudget.activity.login.LoginViewModel
import it.italiancoders.mybudget.activity.main.MainViewModel
import it.italiancoders.mybudget.activity.movements.edit.MovementViewModel
import it.italiancoders.mybudget.activity.movements.list.ListMovementsViewModel
import it.italiancoders.mybudget.activity.registration.RegistrationUserInfoViewModel

@Module
internal abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun provideMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    internal abstract fun provideCategoriesViewModel(viewModel: CategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovementViewModel::class)
    internal abstract fun provideMovementViewModel(viewModel: MovementViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListMovementsViewModel::class)
    internal abstract fun provideListMovementsViewModel(viewModel: ListMovementsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegistrationUserInfoViewModel::class)
    internal abstract fun provideRegistrationUserInfoViewModel(viewModel: RegistrationUserInfoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ActivationViewModel::class)
    internal abstract fun provideActivationViewModel(viewModel: ActivationViewModel): ViewModel
}