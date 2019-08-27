/*
 * Project: mybudget2-mobile-android
 * File: MovementActivity.kt
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

package it.italiancoders.mybudget.activity.movements.edit

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import it.italiancoders.mybudget.R
import it.italiancoders.mybudget.activity.BaseActivity
import it.italiancoders.mybudget.adapters.CategoryAdapter
import it.italiancoders.mybudget.databinding.ActivityMovementBinding
import it.italiancoders.mybudget.manager.categories.CategoriesManager
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.rest.models.Movement
import java.util.*


class MovementActivity : BaseActivity<ActivityMovementBinding>(), View.OnFocusChangeListener {

    companion object {
        const val EXTRA_MOVEMENT_ID = "extraMovementId"
        const val REQUEST_CODE_MOVEMENT = 1000
    }

    val model by lazy { ViewModelProvider(this).get(MovementViewModel::class.java) }

    override fun getLayoutResID(): Int = R.layout.activity_movement

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model

        initToolbar(binding.toolbar)
        initCategoryTextView()

        binding.deleteButton.visibility =
            if (binding.model?.isNewMovement() != true) View.VISIBLE else View.GONE

        if (!model.initialized) {
            model.initialized = true
            val movementId = intent.extras?.getLong(EXTRA_MOVEMENT_ID)

            if (movementId == null) {
                binding.model?.init(Movement())
                setTitle(R.string.movement_new)
            } else {
                MovementsManager(this).load(
                    movementId.toInt(),
                    {
                        binding.model?.init(it ?: Movement())
                        binding.categotyTextView.setText(it?.category?.name)
                    },
                    { binding.model?.init(Movement()) })
                setTitle(R.string.movement_edit)
            }
        }
    }

    fun changeDate(view: View) {
        val calData = Calendar.getInstance()
        model.date.value?.let { calData.time = it }

        DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                val calData = Calendar.getInstance()
                calData.set(selectedYear,selectedMonth,selectedDay)
                model.date.postValue(calData.time)
            },
            calData.get(Calendar.YEAR),
            calData.get(Calendar.MONTH),
            calData.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun changeTime(view: View) {
        val calData = Calendar.getInstance()
        model.date.value?.let { calData.time = it }

        TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinutes ->
                calData.set(Calendar.HOUR_OF_DAY,selectedHour)
                calData.set(Calendar.MINUTE,selectedMinutes)
                model.date.postValue(calData.time)
            },
            calData.get(Calendar.HOUR_OF_DAY),
            calData.get(Calendar.MINUTE),
            true
        ).show()
    }

    fun save(view: View) {
        val movementToSave = binding.model?.getMovement()
        if (movementToSave != null) {
            if (movementToSave.id != null) {
                MovementsManager(this).update(
                    movementToSave.id.toInt(),
                    movementToSave,
                    { finishWithResultOk() },
                    { finishWithResultCanceled() })
            } else {
                MovementsManager(this).create(
                    movementToSave,
                    { finishWithResultOk() },
                    { finishWithResultCanceled() })
            }
        } else {
            MaterialDialog(this).show {
                icon(R.drawable.ic_error)
                title(R.string.attention)
                message(R.string.movement_all_data_required)
            }
        }
    }

    fun delete(view: View) {
        val movementToSave = binding.model?.getMovement()
        if (movementToSave?.id != null) {
            MovementsManager(this).delete(
                movementToSave.id.toInt(),
                { finishWithResultOk() },
                { finishWithResultCanceled() })
        } else
            MaterialDialog(this).show {
                icon(R.drawable.ic_error)
                title(R.string.attention)
                message(R.string.movement_all_data_required)
            }
    }

    private fun finishWithResultOk() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    private fun finishWithResultCanceled() {
        setResult(Activity.RESULT_CANCELED, Intent())
        finish()
    }

    override fun onNetworkStateChange(networkAvailable: Boolean) {
        binding.deleteButton.visibility =
            if (networkAvailable && binding.model?.isNewMovement() != true) View.VISIBLE else View.GONE
        binding.saveButton.visibility = if (networkAvailable) View.VISIBLE else View.GONE
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initCategoryTextView() {
        binding.categotyTextView.threshold = 1
        binding.categotyTextView.setAdapter(CategoryAdapter(this, listOf()))

        CategoriesManager(this).loadAll({
            binding.categotyTextView.setAdapter(CategoryAdapter(this, it ?: listOf()))
        }, {
            binding.categotyTextView.setAdapter(CategoryAdapter(this, listOf()))
        })

        binding.categotyTextView.onItemClickListener =
            OnItemClickListener { parent, _, pos, _ ->
                binding.model?.category?.postValue(parent.adapter.getItem(pos) as Category?)
            }

        //we have to add check for 0 number of character in edit text. When that
        //happens, we will show pop up manually
        binding.categotyTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //first check if length of input is 0
                if (s?.length ?: 0 == 0) {
                    //if you don't use handler with post delay, the API will hide pop
                    //up, even if you show it. There could be better ways to this, but
                    //I have implemented this and after 100 millis it gives an animated
                    //look
                    Handler().postDelayed({
                        //manually show drop down
                        binding.categotyTextView.showDropDown()
                    }, 100) // with 100 millis of delay
                }
            }
        })
        //when user focus out the view, drop down vanishes. When come back it will not
        //show, so to cover this scenario add following.
        binding.categotyTextView.onFocusChangeListener = this@MovementActivity
    }


    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        Handler().postDelayed({
            if (hasFocus && !this@MovementActivity.isFinishing)
                binding.categotyTextView.showDropDown()
        }, 500)
    }
}