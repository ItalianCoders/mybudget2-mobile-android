/*
 * Project: mybudget2-mobile-android
 * File: EditCategoryDialog.kt
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

package it.italiancoders.mybudget.activity.categories.edit

import android.content.Context
import com.afollestad.materialdialogs.DialogCallback
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.SessionData
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.rest.models.Category

/**
 * @author fattazzo
 *         <p/>
 *         date: 22/07/19
 */
class EditCategoryDialogBuilder() {

    fun build(context: Context, category: Category = Category()): MaterialDialog {

        val dialog = MaterialDialog(context, BottomSheet(LayoutMode.WRAP_CONTENT))
        dialog.noAutoDismiss()

        dialog.customView(R.layout.item_category)

        // Icon and title
        dialog.icon(R.drawable.ic_categories)
        dialog.title(if (category.id == null) R.string.new_category else R.string.edit_category)

        // TextView
        dialog.getCustomView().findViewById<TextInputEditText>(R.id.nameTextView).setText(category.name)
        dialog.getCustomView().findViewById<TextInputEditText>(R.id.descriptionTextView)
            .setText(category.description)

        // Buttons
        if(SessionData.networkAvailable.value != false) {
            dialog.positiveButton(R.string.save, click = getPositiveCallback(category))
            // Add delete button only for existing and not readonly categories
            if (category.id != null && !category.isIsReadOnly) {
                dialog.negativeButton(R.string.delete, click = getNegativeCallback(category))
            }
        }

        return dialog
    }

    /**
     * Negative callback creator. The callback delete the category from remote repository
     */
    private fun getNegativeCallback(category: Category): DialogCallback {
        return object : DialogCallback {
            override fun invoke(dialog: MaterialDialog) {
                if (category.id != null)
                    CategoriesManager(dialog.context).delete(category.id.toInt())
                dialog.dismiss()
            }
        }
    }

    /**
     * Positive callback creator. The callback add o update the category to remote repository
     */
    private fun getPositiveCallback(category: Category): DialogCallback {
        return object : DialogCallback {
            override fun invoke(dialog: MaterialDialog) {

                val nameTIL = dialog.getCustomView().findViewById<TextInputLayout>(R.id.nameTextInputLayout)
                val descriptionTIL =
                    dialog.getCustomView().findViewById<TextInputLayout>(R.id.descriptionTextInputLayout)
                validateView(nameTIL, descriptionTIL)

                if (nameTIL.editText?.error == null && descriptionTIL.editText?.error == null) {
                    category.name = nameTIL.editText?.text.toString()
                    category.description = descriptionTIL.editText?.text.toString()

                    if (category.id != null) {
                        CategoriesManager(dialog.context)
                            .update(category.id.toInt(), category)
                    } else {
                        CategoriesManager(dialog.context).create(category)
                    }
                    dialog.dismiss()
                }
            }
        }
    }

    private fun validateView(nameTIL: TextInputLayout, descriptionTIL: TextInputLayout) {

        if (nameTIL.editText?.text.isNullOrBlank()) {
            nameTIL.editText?.error = nameTIL.context.getString(R.string.field_required)
        } else {
            nameTIL.editText?.error = null
        }

        if (descriptionTIL.editText?.text.isNullOrBlank()) {
            descriptionTIL.editText?.error = descriptionTIL.context.getString(R.string.field_required)
        } else {
            descriptionTIL.editText?.error = null
        }
    }
}