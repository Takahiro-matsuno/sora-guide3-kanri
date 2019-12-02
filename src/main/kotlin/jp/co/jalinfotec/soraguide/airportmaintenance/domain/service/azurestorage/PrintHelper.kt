package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.azurestorage

import com.microsoft.azure.storage.StorageException
import java.io.PrintWriter
import java.io.StringWriter

/*
  Copyright Microsoft Corporation
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  http://www.apache.org/licenses/LICENSE-2.0
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */



/**
 * A class which provides utility methods
 *
 */
internal object PrintHelper {
    /**
     * Prints out the sample start information .
     */
    fun printSampleStartInfo(sampleName: String?) {
        println(String.format(
                "%s samples starting...",
                sampleName))
    }

    /**
     * Prints out the sample complete information .
     */
    fun printSampleCompleteInfo(sampleName: String?) {
        println(String.format(
                "%s samples completed.",
                sampleName))
    }

    /**
     * Print the exception stack trace
     *
     * @param t Exception to be printed
     */
    fun printException(t: Throwable) {
        val stringWriter = StringWriter()
        val printWriter = PrintWriter(stringWriter)
        t.printStackTrace(printWriter)
        if (t is StorageException) {
            if (t.extendedErrorInformation != null) {
                println(String.format("\nError: %s", t.extendedErrorInformation.errorMessage))
            }
        }
        println(String.format("Exception details:\n%s", stringWriter.toString()))
    }
}