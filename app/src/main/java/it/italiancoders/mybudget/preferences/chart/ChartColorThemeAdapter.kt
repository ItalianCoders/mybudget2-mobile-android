/*
 * Project: mybudget2-mobile-android
 * File: ChartColorThemeAdapter.kt
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

package it.italiancoders.mybudget.preferences.chart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import it.italiancoders.mybudget.R
import kotlinx.android.synthetic.main.preference_list_chart_theme_picker.view.*

/**
 * @author fattazzo
 *         <p/>
 *         date: 26/08/19
 */
class ChartColorThemeAdapter(val context: Context, var currentIndex: Int) :
    RecyclerView.Adapter<ChartColorThemeAdapter.ViewHolder>() {

    val items = ChartColorTheme.values()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.preference_list_chart_theme_picker,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val theme = items[position]

        holder.image1.setBackgroundColor(theme.colors[0])
        holder.image2.setBackgroundColor(theme.colors[1])
        holder.image3.setBackgroundColor(theme.colors[2])
        holder.image4.setBackgroundColor(theme.colors[3])
        holder.image5.setBackgroundColor(theme.colors[4])
        holder.iconName.text = theme.name

        holder.radioButton.isChecked = currentIndex == theme.ordinal
        holder.itemView.tag = theme.ordinal

        holder.itemView.setOnClickListener {
            currentIndex = it.tag as Int
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var image1: ImageView = view.color1
        internal var image2: ImageView = view.color2
        internal var image3: ImageView = view.color3
        internal var image4: ImageView = view.color4
        internal var image5: ImageView = view.color5
        internal var iconName: TextView = view.iconName
        internal var radioButton: RadioButton = view.radioButton
    }
}