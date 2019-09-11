/*
 * Project: mybudget2-mobile-android
 * File: CategoriesActivityTest.kt
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

import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import it.italiancoders.mybudget.activity.BaseActivityTest
import it.italiancoders.mybudget.mocks.config.CategoriesConfig
import it.italiancoders.mybudget.mocks.data.CategoriesMockData
import it.italiancoders.mybudget.rest.models.Category
import it.italiancoders.mybudget.tutorial.TutorialCategoriesActivity
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

/**
 * @author fattazzo
 *         <p/>
 *         date: 02/09/19
 */
class CategoriesActivityTest : BaseActivityTest() {

    @Rule
    @JvmField
    var rule: ActivityTestRule<CategoriesActivity> =
        ActivityTestRule(CategoriesActivity::class.java, true, false)

    private val categoriesPageObject = CategoriesPageObject()

    override fun getActivityTutorialKey(): String? = TutorialCategoriesActivity.KEY

    override fun isTutorialAlreadyShow(): Boolean = true

    @Test
    fun refreshValues() {

        CategoriesMockData.mockLoadAll_dataOk(categoriesManager)

        val expectedCategories: List<Category> =
            CategoriesMockData.listFromJsonFile(CategoriesConfig.DATA_ALL_OK, Category::class)

        rule.launchActivity(null)
        val viewModel = rule.activity.categoriesViewModel

        assertThat(viewModel.categories.value, `is`(notNullValue()))
        assertThat(viewModel.categories.value!!.size, `is`(expectedCategories.size))
        categoriesPageObject.checkCategoriesListChildCount(expectedCategories.size)

        // Mock data. The refresh load all without category with id 2
        Mockito.`when`(categoriesManager.loadAll(any()))
            .doReturn(
                CategoriesMockData.listFromJsonFile(
                    CategoriesConfig.DATA_NO_ID_2_OK,
                    Category::class
                )
            )

        categoriesPageObject.refresh()

        assertThat(viewModel.categories.value, `is`(notNullValue()))
        assertThat(viewModel.categories.value!!.size, `is`(expectedCategories.size - 1))
        categoriesPageObject.checkCategoriesListChildCount(expectedCategories.size - 1)


    }

    @Test
    fun createCategory() {

        CategoriesMockData.mockLoadAll_dataOk(categoriesManager)

        rule.launchActivity(null)
        val viewModel = rule.activity.categoriesViewModel

        var expectedCategories: List<Category> =
            CategoriesMockData.listFromJsonFile(CategoriesConfig.DATA_ALL_OK, Category::class)
        assertThat(viewModel.categories.value!!.size, `is`(expectedCategories.size))
        categoriesPageObject.checkCategoriesListChildCount(expectedCategories.size)

        categoriesPageObject.clickNewCategory()

        categoriesPageObject.checkNewCategoryDialogVisibility(true)

        categoriesPageObject.checkCategoryName("")
        categoriesPageObject.checkCategoryDescription("")
        categoriesPageObject.setCategoryName("New category")
        categoriesPageObject.setCategoryDescription("New category")
        categoriesPageObject.checkCategoryName("New category")
        categoriesPageObject.checkCategoryDescription("New category")

        // Mock data. After click on save button the activity refresh all
        Mockito.`when`(categoriesManager.loadAll(any()))
            .doReturn(
                CategoriesMockData.listFromJsonFile(
                    CategoriesConfig.DATA_NEW_ID_10_OK,
                    Category::class
                )
            )

        categoriesPageObject.clickSaveCategoryButton()

        categoriesPageObject.checkNewCategoryDialogVisibility(false)

        expectedCategories = CategoriesMockData.listFromJsonFile(CategoriesConfig.DATA_NEW_ID_10_OK, Category::class)
        assertThat(viewModel.categories.value!!.size, `is`(expectedCategories.size))
        categoriesPageObject.checkCategoriesListChildCount(expectedCategories.size)
    }

    @Test
    fun updateCategory() {

        CategoriesMockData.mockLoadAll_dataOk(categoriesManager)

        rule.launchActivity(null)
        val viewModel = rule.activity.categoriesViewModel

        val expectedCategories: List<Category> =
            CategoriesMockData.listFromJsonFile(CategoriesConfig.DATA_ALL_OK, Category::class)
        assertThat(viewModel.categories.value!!.size, `is`(expectedCategories.size))
        categoriesPageObject.checkCategoriesListChildCount(expectedCategories.size)

        CategoriesMockData.mockId00002(categoriesManager)

        categoriesPageObject.clickCategoryItem(3)

        categoriesPageObject.checkEditCategoryDialogVisibility(true)
        categoriesPageObject.checkCategoryName("Shopping")
        categoriesPageObject.checkCategoryDescription("Shopping")

        // Mock data. After click on save button the activity refresh all
        Mockito.`when`(categoriesManager.loadAll(any()))
            .doReturn(
                CategoriesMockData.listFromJsonFile(
                    CategoriesConfig.DATA_ID_2_UPDATED_OK,
                    Category::class
                )
            )

        categoriesPageObject.clickSaveCategoryButton()

        categoriesPageObject.checkNewCategoryDialogVisibility(false)

        val expectedUpdateCategories : List<Category> = CategoriesMockData.listFromJsonFile(CategoriesConfig.DATA_ID_2_UPDATED_OK, Category::class)
        assertThat(expectedCategories.size, `is`(expectedUpdateCategories.size))
        assertThat(viewModel.categories.value!!.size, `is`(expectedUpdateCategories.size))
        categoriesPageObject.checkCategoriesListChildCount(expectedUpdateCategories.size)

        assertThat(expectedCategories.find { it.id!! == 2L }?.name, not(`is`(expectedUpdateCategories.find { it.id!! == 2L }?.name)))
        assertThat(expectedCategories.find { it.id!! == 2L }?.description, not(`is`(expectedUpdateCategories.find { it.id!! == 2L }?.description)))
    }

    @Test
    fun deleteCategory() {

        CategoriesMockData.mockLoadAll_dataOk(categoriesManager)

        rule.launchActivity(null)
        val viewModel = rule.activity.categoriesViewModel

        val expectedCategories: List<Category> =
            CategoriesMockData.listFromJsonFile(CategoriesConfig.DATA_ALL_OK, Category::class)
        assertThat(viewModel.categories.value!!.size, `is`(expectedCategories.size))
        categoriesPageObject.checkCategoriesListChildCount(expectedCategories.size)

        CategoriesMockData.mockId00002(categoriesManager)

        categoriesPageObject.clickCategoryItem(3)

        categoriesPageObject.checkEditCategoryDialogVisibility(true)
        categoriesPageObject.checkCategoryName("Shopping")
        categoriesPageObject.checkCategoryDescription("Shopping")

        // Mock data. After click on save button the activity refresh all
        Mockito.`when`(categoriesManager.loadAll(any()))
            .doReturn(
                CategoriesMockData.listFromJsonFile(
                    CategoriesConfig.DATA_NO_ID_2_OK,
                    Category::class
                )
            )

        categoriesPageObject.clickDeleteCategoryButton()

        categoriesPageObject.checkNewCategoryDialogVisibility(false)

        val expectedDeleteCategories : List<Category> = CategoriesMockData.listFromJsonFile(CategoriesConfig.DATA_NO_ID_2_OK, Category::class)
        assertThat(expectedCategories.size, `is`(expectedDeleteCategories.size+1))
        assertThat(viewModel.categories.value!!.size, `is`(expectedDeleteCategories.size))
        categoriesPageObject.checkCategoriesListChildCount(expectedDeleteCategories.size)

        assertThat(viewModel.categories.value!!.find { it.id!! == 2L }, `is`(nullValue()))
    }
}