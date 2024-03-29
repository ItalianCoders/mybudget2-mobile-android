/*
 * Project: mybudget2-mobile-android
 * File: MovementsMockData.kt
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

package it.italiancoders.mybudget.mocks.data

import com.nhaarman.mockitokotlin2.doReturn
import it.italiancoders.mybudget.app.AppConstants
import it.italiancoders.mybudget.manager.movements.MovementsManager
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.mocks.config.MovementsConfig
import it.italiancoders.mybudget.rest.models.Movement
import it.italiancoders.mybudget.rest.models.MovementListPage
import org.mockito.Mockito.`when`

/**
 * @author fattazzo
 *         <p/>
 *         date: 04/09/19
 */
object MovementsMockData : AbstractMockData() {

    fun mock2019_08_page0(movementsManager: MovementsManager) {
        val parametri =
            ParametriRicerca(2019, 8, null, null, null, 0, AppConstants.DEFAULT_PAGE_SIZE, null)

        `when`(movementsManager.search(parametri))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_PAGE_0_OK, MovementListPage::class))
    }

    fun mock2019_08_page1(movementsManager: MovementsManager) {
        val parametri =
            ParametriRicerca(2019, 8, null, null, null, 1, AppConstants.DEFAULT_PAGE_SIZE, null)

        `when`(movementsManager.search(parametri))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_PAGE_1_OK, MovementListPage::class))
    }

    fun mock2019_08(movementsManager: MovementsManager) {
        val parametri =
            ParametriRicerca(2019, 8, null, null, null, 0, AppConstants.DEFAULT_PAGE_SIZE, null)

        `when`(movementsManager.search(parametri))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_OK, MovementListPage::class))

        `when`(movementsManager.search(parametri, false))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_OK, MovementListPage::class))

        `when`(movementsManager.search(parametri, true))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_OK, MovementListPage::class))
    }

    fun mock2019_08_no_id_00014(movementsManager: MovementsManager) {
        val parametri =
            ParametriRicerca(2019, 8, null, null, null, 0, AppConstants.DEFAULT_PAGE_SIZE, null)

        `when`(movementsManager.search(parametri))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_NO_ID_00014_OK, MovementListPage::class))

        `when`(movementsManager.search(parametri, false))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_NO_ID_00014_OK, MovementListPage::class))

        `when`(movementsManager.search(parametri, true))
            .doReturn(fromJsonFile(MovementsConfig.DATA_2019_08_NO_ID_00014_OK, MovementListPage::class))
    }

    fun mock_id_00014(movementsManager: MovementsManager) {

        `when`(movementsManager.load(14))
            .doReturn(fromJsonFile(MovementsConfig.DATA_ID_00014_OK, Movement::class))
    }
}