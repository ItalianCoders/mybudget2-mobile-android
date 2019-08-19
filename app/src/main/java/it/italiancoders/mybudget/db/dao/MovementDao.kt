/*
 * Project: mybudget2-mobile-android
 * File: MovementDao.kt
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

package it.italiancoders.mybudget.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.italiancoders.mybudget.db.entity.Movement

/**
 * @author fattazzo
 *         <p/>
 *         date: 23/07/19
 */
@Dao
interface MovementDao {

    @Query("SELECT * from movements where executedAt like :dateFilter || '%' LIMIT :page,:size")
    fun search(dateFilter: String, page: Int, size: Int): List<Movement>

    @Query("SELECT count(id) from movements where executedAt like :dateFilter || '%'")
    fun count(dateFilter: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movement: Movement)

    @Query("DELETE FROM movements where executedAt like :dateFilter || '%'")
    fun delete(dateFilter: String)

    @Query("DELETE FROM movements where id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM movements where id = :id")
    fun load(id: Long): Movement?
}