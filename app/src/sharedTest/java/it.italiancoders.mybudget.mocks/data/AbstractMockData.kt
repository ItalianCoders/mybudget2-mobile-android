/*
 * Project: mybudget2-mobile-android
 * File: AbstractMockData.kt
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

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.beust.klaxon.*
import org.junit.Assert.fail
import java.io.File
import java.io.InputStream
import kotlin.reflect.KClass


/**
 * @author fattazzo
 *         <p/>
 *         date: 02/09/19
 */
abstract class AbstractMockData {

    fun <T> fromJsonFile(filePath: String, kclazz: KClass<*>): T? {
        return try {
            val parser = Klaxon().parser(kclazz)
            val parseValue = parser.parse(loadFile(filePath))

            var result : T? = null
            if(parseValue is JsonObject) {
                result = Klaxon().fromJsonObject(
                    parser.parse(loadFile(filePath)) as JsonObject,
                    T::class.java,
                    kclazz
                ) as T?
            }
            result
        } catch (e: Exception) {
            fail("Error json parse from File $filePath to ${kclazz.simpleName}")
            null
        }
    }

    fun <T> listFromJsonFile(filePath: String, kclazz: KClass<*>): List<T> {
        return try {
            val parser = Klaxon().parser(kclazz)
            val parseValue = parser.parse(loadFile(filePath))

            val result = arrayListOf<Any?>()

            val jsonArray = parseValue as JsonArray<T>

            jsonArray.forEach { jo ->
                if (jo is JsonObject) {
                    val t = Klaxon().fromJsonObject(jo as JsonObject,kclazz::class.java,kclazz)
                    if (t != null) result.add(t)
                    else throw KlaxonException("Couldn't convert $jo")
                } else if (jo != null) {
                    val converter = Klaxon().findConverterFromClass(T::class.java, null)
                    val convertedValue = converter.fromJson(JsonValue(jo, null, null, Klaxon()))
                    result.add(convertedValue)
                } else {
                    throw KlaxonException("Couldn't convert $jo")
                }
            }
            @Suppress("UNCHECKED_CAST")
            result as List<T>
        } catch (e: Exception) {
            fail("Error json parse from File $filePath to ${kclazz.simpleName}")
            listOf()
        }
    }

    /**
     * Load file from resources folder. Resource folder mapping instrumental test assets.
     *
     * See build.gradle in sourceSets section
     */
    private fun loadFile2(filePath: String): File {
        val fileUri = try {
            this.javaClass.classLoader?.getResource(filePath)?.toURI()
        } catch (e: java.lang.Exception) {
            null
        }

        assert(fileUri != null) { "Resource: $filePath not found" }

        return File(fileUri)
    }

    private fun loadFile(filePath: String): InputStream {
        val stream = try {
            this.javaClass.classLoader?.getResourceAsStream(filePath)
        } catch (e: java.lang.Exception) {
            null
        }

        assert(stream != null) { "Resource: $filePath not found" }

        return stream!!
    }
}