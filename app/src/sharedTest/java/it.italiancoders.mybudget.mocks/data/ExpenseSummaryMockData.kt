/*
 * Project: mybudget2-mobile-android
 * File: ExpenseSummaryMockData.kt
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
import it.italiancoders.mybudget.manager.expensesummary.ExpenseSummaryManager
import it.italiancoders.mybudget.manager.movements.ParametriRicerca
import it.italiancoders.mybudget.mocks.config.ExpenseSummaryConfig
import it.italiancoders.mybudget.rest.models.ExpenseSummary
import org.mockito.Mockito.`when`

/**
 * @author fattazzo
 *         <p/>
 *         date: 30/08/19
 */
object ExpenseSummaryMockData : AbstractMockData() {

    fun mockData(
        expenseSummaryManager: ExpenseSummaryManager,
        parametriRicerca: ParametriRicerca,
        dataJsonPath: String
    ) {
        `when`(expenseSummaryManager.getExpenseSummary(parametriRicerca,true))
            .doReturn(fromJsonFile<ExpenseSummary>(dataJsonPath, ExpenseSummary::class))
        `when`(expenseSummaryManager.getExpenseSummary(parametriRicerca,false))
            .doReturn(fromJsonFile<ExpenseSummary>(dataJsonPath, ExpenseSummary::class))
    }


    fun mock2019_07_dataOk(expenseSummaryManager: ExpenseSummaryManager) {
        mockData(
            expenseSummaryManager,
            ParametriRicerca(2019, 7),
            ExpenseSummaryConfig.DATA_2019_07_OK
        )
    }

    fun mock2019_08_dataOk(expenseSummaryManager: ExpenseSummaryManager) {
        mockData(
            expenseSummaryManager,
            ParametriRicerca(2019, 8),
            ExpenseSummaryConfig.DATA_2019_08_OK
        )
    }

    fun mock2019_08_week_2_dataOk(expenseSummaryManager: ExpenseSummaryManager) {
        mockData(
            expenseSummaryManager,
            ParametriRicerca(2019, 8, 2),
            ExpenseSummaryConfig.DATA_2019_08_WEEK_2_OK
        )
    }

    fun mock2019_08_week_3_dataOk(expenseSummaryManager: ExpenseSummaryManager) {
        mockData(
            expenseSummaryManager,
            ParametriRicerca(2019, 8, 3),
            ExpenseSummaryConfig.DATA_2019_08_WEEK_3_OK
        )
    }

    fun mock2019_08_day_06_dataOk(expenseSummaryManager: ExpenseSummaryManager) {
        mockData(
            expenseSummaryManager,
            ParametriRicerca(2019, 8, 6, null, null),
            ExpenseSummaryConfig.DATA_2019_08_DAY_06_OK
        )
    }

    fun mock2019_08_day_14_dataOk(expenseSummaryManager: ExpenseSummaryManager) {
        mockData(
            expenseSummaryManager,
            ParametriRicerca(2019, 8, 14, null, null),
            ExpenseSummaryConfig.DATA_2019_08_DAY_14_OK
        )
    }

    fun mock2019_10_dataOk_empty(expenseSummaryManager: ExpenseSummaryManager) {
        mockData(
            expenseSummaryManager,
            ParametriRicerca(2019, 10),
            ExpenseSummaryConfig.DATA_OK_EMPTY
        )
    }
}