package com.challenge.colour_spotter.network.data

import androidx.annotation.VisibleForTesting
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

/**
 * This class helps with loading Android `/assets` files, especially when running JVM unit tests.
 * It must remain on the root package for an easier [Class.getResource] with relative paths.
 * @see <a href="https://developer.android.com/reference/tools/gradle-api/7.3/com/android/build/api/dsl/UnitTestOptions">UnitTestOptions</a>
 */
@VisibleForTesting
internal object JvmUnitTestFakeAssetManager {
    fun open(fileName: String): String = readFileWithNewLineFromResources(fileName)

    @Throws(IOException::class)
    fun readFileWithNewLineFromResources(fileName: String): String {
        var inputStream: InputStream? = null
        try {
            inputStream = existFile(fileName)
            val builder = StringBuilder()
            val reader = BufferedReader(InputStreamReader(inputStream))

            var theCharNum = reader.read()
            while (theCharNum != -1) {
                builder.append(theCharNum.toChar())
                theCharNum = reader.read()
            }

            return builder.toString()
        } finally {
            inputStream?.close()
        }
    }

    fun existFile(fileName: String): InputStream? {
        return javaClass.classLoader?.getResourceAsStream(fileName)
    }
}
