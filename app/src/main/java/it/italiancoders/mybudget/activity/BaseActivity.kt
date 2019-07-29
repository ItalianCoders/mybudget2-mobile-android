/*
 * Project: mybudget2-mobile-android
 * File: BaseActivity.kt
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

package it.italiancoders.mybudget.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.SessionData


/**
 * @author fattazzo
 *         <p/>
 *         date: 01/07/19
 */
abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private var menu: Menu? = null

    private val networkAvailabilityObserver  by lazy { Observer<Boolean> { syncNetworkStateOption() } }

    /**
     * Activity data binding
     */
    protected val binding: T by lazy {
        val bind = DataBindingUtil.setContentView<T>(this, getLayoutResID())
        bind.lifecycleOwner = this
        bind
    }

    /**
     * Activity layout ID
     * @return activity layout ID
     */
    protected abstract fun getLayoutResID(): Int

    /**
     * Tint color of all menu items icon
     */
    open fun getMenuItemsIconColor() : Int = android.R.color.white

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SessionData.networkAvailable.observe(this,networkAvailabilityObserver)
    }

    override fun onDestroy() {
        SessionData.networkAvailable.removeObserver(networkAvailabilityObserver)
        super.onDestroy()
    }

    private fun syncNetworkStateOption() {
        if (SessionData.networkAvailable.value == false) {
            showOption(R.id.action_network_offline)
        } else {
            hideOption(R.id.action_network_offline)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.base, menu)
        this.menu = menu

        var drawable = menu.findItem(R.id.action_network_offline).icon

        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, getMenuItemsIconColor()))
        menu.findItem(R.id.action_network_offline).icon = drawable

        syncNetworkStateOption()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_network_offline -> {
                MaterialDialog(this).show {
                    icon(R.drawable.ic_signal_cellular_off)
                    title(R.string.network_unavailable_dialog_title)
                    message(R.string.network_unavailable_dialog_message)
                    positiveButton(android.R.string.ok)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    protected fun hideOption(id: Int) {
        menu?.findItem(id)?.isVisible = false
    }

    protected fun showOption(id: Int) {
        menu?.findItem(id)?.isVisible = true
    }
}