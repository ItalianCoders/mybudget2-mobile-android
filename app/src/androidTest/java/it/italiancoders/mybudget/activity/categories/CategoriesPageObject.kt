/*
 * Project: mybudget2-mobile-android
 * File: CategoriesPageObject.kt
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

package it.italiancoders.mybudget.activity.categories

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.afollestad.materialdialogs.internal.main.DialogLayout
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.ViewActions.setTextInTextView
import it.italiancoders.mybudget.ViewActions.withCustomConstraints
import org.hamcrest.CoreMatchers

/**
 * @author fattazzo
 *         <p/>
 *         date: 09/09/19
 */
class CategoriesPageObject {

    fun refresh() {
        // SwipeRefreshLayout is not displayed al least 90% and perform swipeDown action cause:
        //
        // android.support.test.espresso.PerformException: Error performing 'fast swipe'
        // on view 'with id: my.app.package:id/my_refresh_layout'.
        // ...
        // Caused by: java.lang.RuntimeException: Action will not be performed because the target view
        // does not match one or more of the following constraints:
        // at least 90 percent of the view's area is displayed to the user
        //
        // ViewActions.withCustomConstraints wrap the swipe action into another action and override these constraints by hand
        onView(withId(R.id.swipeRefreshContainer)).perform(
            withCustomConstraints(swipeDown(), isDisplayingAtLeast(20))
        )
    }

    fun checkCategoriesListChildCount(count: Int) {
        onView(withId(R.id.categoriesRecyclerView)).check(matches(hasChildCount(count)))
    }

    fun clickNewCategory() {
        onView(withId(R.id.fab)).perform(click())
    }

    fun checkNewCategoryDialogVisibility(visible : Boolean) {

        if (visible) {
            onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(matches(isDisplayed()))
            onView(withId(R.id.md_text_title)).check(matches(withText(R.string.new_category)))
        } else {
            onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(doesNotExist())
        }
    }

    fun checkEditCategoryDialogVisibility(visible : Boolean) {

        if (visible) {
            onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(matches(isDisplayed()))
            onView(withId(R.id.md_text_title)).check(matches(withText(R.string.edit_category)))
        } else {
            onView(CoreMatchers.instanceOf(DialogLayout::class.java)).check(doesNotExist())
        }
    }

    fun setCategoryName(name: String) {
        onView(withId(R.id.nameTextView)).perform(setTextInTextView(name))
    }

    fun checkCategoryName(name: String) {
        onView(withId(R.id.nameTextView)).check(matches(withText(name)))
    }

    fun setCategoryDescription(description: String) {
        onView(withId(R.id.descriptionTextView)).perform(setTextInTextView(description))
    }

    fun checkCategoryDescription(description: String) {
        onView(withId(R.id.descriptionTextView)).check(matches(withText(description)))
    }

    fun clickSaveCategoryButton() {
        onView(withId(R.id.md_button_positive)).perform(click())
    }

    fun clickCategoryItem(position: Int) {
        onView(withId(R.id.categoriesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition<CategoriesDataAdapter.CategoryViewHolder>(position,click()))
    }

    fun clickDeleteCategoryButton() {
        onView(withId(R.id.md_button_negative)).perform(click())
    }
}