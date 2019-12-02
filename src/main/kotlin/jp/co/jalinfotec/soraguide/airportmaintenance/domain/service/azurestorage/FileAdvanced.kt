package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.azurestorage

import com.microsoft.azure.storage.CorsHttpMethods
import com.microsoft.azure.storage.CorsRule
import com.microsoft.azure.storage.MetricsLevel
import com.microsoft.azure.storage.StorageException
import com.microsoft.azure.storage.file.CloudFileClient
import com.microsoft.azure.storage.file.FileServiceProperties
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.azurestorage.DataGenerator.createRandomName
import java.io.IOException
import java.net.URISyntaxException
import java.security.InvalidKeyException
import java.util.*
import kotlin.collections.HashMap

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
 * This sample illustrates advanced usage of the Azure file storage service.
 */
internal class FileAdvanced {
    /**
     * Executes the samples.
     *
     * @throws URISyntaxException Uri has invalid syntax
     * @throws InvalidKeyException Invalid key
     */
    @Throws(InvalidKeyException::class, URISyntaxException::class, IOException::class)
    fun runSamples() {
        println()
        println()
        PrintHelper.printSampleStartInfo("File Advanced")
        // Create a file service client
        val fileClient: CloudFileClient = FileClientProvider.fileClientReference
        try {
            println("Service properties sample")
            serviceProperties(fileClient)
            println()
            println("CORS sample")
            corsRules(fileClient)
            println()
            println("Share properties sample")
            shareProperties(fileClient)
            println()
            println("Share metadata sample")
            shareMetadata(fileClient)
            println()
            println("Directory properties sample")
            directoryProperties(fileClient)
            println()
            println("Directory metadata sample")
            directoryMetadata(fileClient)
            println()
            println("File properties sample")
            fileProperties(fileClient)
            println()
            println("File metadata sample")
            fileMetadata(fileClient)
            println()
        } catch (t: Throwable) {
            PrintHelper.printException(t)
        }
        PrintHelper.printSampleCompleteInfo("File Advanced")
    }

    /**
     * Manage the service properties including hour and minute metrics.
     * @param fileClient Azure Storage File Service
     */
    @Throws(StorageException::class)
    private fun serviceProperties(fileClient: CloudFileClient) {
        println("Get service properties")
        val originalProps = fileClient.downloadServiceProperties()
        try {
            println("Set service properties")
            // Change service properties
            val props = FileServiceProperties()
            val hours = props.hourMetrics
            hours.metricsLevel = MetricsLevel.SERVICE_AND_API
            hours.retentionIntervalInDays = 1
            hours.version = "1.0"
            val minutes = props.minuteMetrics
            minutes.metricsLevel = MetricsLevel.SERVICE
            minutes.retentionIntervalInDays = 1
            minutes.version = "1.0"
            fileClient.uploadServiceProperties(props)
            println()
            println("Hour Metrics")
            System.out.printf("version: %s%n", props.hourMetrics.version)
            System.out.printf("retention interval: %d%n", props.hourMetrics.retentionIntervalInDays)
            System.out.printf("operation types: %s%n", props.hourMetrics.metricsLevel)
            println()
            println("Minute Metrics")
            System.out.printf("version: %s%n", props.minuteMetrics.version)
            System.out.printf("retention interval: %d%n", props.minuteMetrics.retentionIntervalInDays)
            System.out.printf("operation types: %s%n", props.minuteMetrics.metricsLevel)
            println()
        } finally { // Revert back to original service properties
            fileClient.uploadServiceProperties(originalProps)
        }
    }

    /**
     * Set CORS rules sample.
     * @param fileClient Azure Storage Blob Service
     */
    @Throws(StorageException::class)
    private fun corsRules(fileClient: CloudFileClient) {
        val originalProperties = fileClient.downloadServiceProperties()
        try { // Set CORS rules
            println("Set CORS rules")
            val ruleAllowAll = CorsRule()
            ruleAllowAll.allowedOrigins.add("*")
            ruleAllowAll.allowedMethods.add(CorsHttpMethods.GET)
            ruleAllowAll.allowedHeaders.add("*")
            ruleAllowAll.exposedHeaders.add("*")
            val props = fileClient.downloadServiceProperties()
            props.cors.corsRules.add(ruleAllowAll)
            fileClient.uploadServiceProperties(props)
        } finally { // Revert back to original service properties
            fileClient.uploadServiceProperties(originalProperties)
        }
    }

    /**
     * Manage Share Properties
     *
     * @param fileClient Azure Storage File Service
     */
    @Throws(URISyntaxException::class, StorageException::class)
    private fun shareProperties(fileClient: CloudFileClient) { // Create share
        val fileShareName = createRandomName("share-")
        val fileShare = fileClient.getShareReference(fileShareName)
        try {
            println("Create share")
            fileShare.createIfNotExists()
            // Set share properties
            println("Set share properties")
            fileShare.properties.shareQuota = 10
            fileShare.uploadProperties()
            // Get share properties
            println("Get share properties")
            val props = fileShare.properties
            println()
            System.out.printf("share quota: %s%n", props.shareQuota)
            System.out.printf("last modified: %s%n", props.lastModified)
            System.out.printf("Etag: %s%n", props.etag)
            println()
        } finally { // Delete share
            println("Delete share")
            fileShare.deleteIfExists()
        }
    }

    /**
     * Manage Share Metadata
     *
     * @param fileClient Azure Storage File Service
     */
    @Throws(URISyntaxException::class, StorageException::class)
    private fun shareMetadata(fileClient: CloudFileClient) {
        val fileShareName = createRandomName("share-")
        val fileShare = fileClient.getShareReference(fileShareName)
        try { // Set share metadata
            println("Set share metadata")
            var metadata = HashMap<String?, String?>()
            metadata["key1"] = "value1"
            metadata["foo"] = "bar"
            fileShare.metadata = metadata
            // Create share
            println("Create share")
            fileShare.createIfNotExists()
            // Get share metadata
            println("Get share metadata")
            metadata = fileShare.metadata
            val it: MutableIterator<*> = metadata.entries.iterator()
            while (it.hasNext()) {
                val pair = it.next() as Map.Entry<*, *>
                System.out.printf(" %s = %s%n", pair.key, pair.value)
                it.remove()
            }
        } finally { // Delete share
            println("Delete share")
            fileShare.deleteIfExists()
        }
    }

    /**
     * Get Directory Properties
     *
     * @param fileClient Azure Storage File Service
     */
    @Throws(URISyntaxException::class, StorageException::class)
    private fun directoryProperties(fileClient: CloudFileClient) {
        val fileShareName = createRandomName("share-")
        val fileShare = fileClient.getShareReference(fileShareName)
        try { // Create share
            println("Create share")
            fileShare.createIfNotExists()
            // Create directory
            println("Create directory")
            val rootDir = fileShare.rootDirectoryReference
            val dir = rootDir.getDirectoryReference("folder")
            dir.createIfNotExists()
            // Get directory properties
            println("Get directory properties")
            val props = dir.properties
            println()
            System.out.printf("last modified: %s%n", props.lastModified)
            System.out.printf("Etag: %s%n", props.etag)
            println()
        } finally { // Delete share
            println("Delete share")
            fileShare.deleteIfExists()
        }
    }

    /**
     * Manage Directory Metadata
     *
     * @param fileClient Azure Storage File Service
     */
    @Throws(URISyntaxException::class, StorageException::class)
    private fun directoryMetadata(fileClient: CloudFileClient) { // Create share
        val fileShareName = createRandomName("share-")
        val fileShare = fileClient.getShareReference(fileShareName)
        try {
            println("Create share")
            fileShare.createIfNotExists()
            val rootDir = fileShare.rootDirectoryReference
            val dir = rootDir.getDirectoryReference("folder")
            dir.createIfNotExists()
            // Set directory metadata
            println("Set directory metadata")
            var metadata = HashMap<String?, String?>()
            metadata["key1"] = "value1"
            metadata["foo"] = "bar"
            dir.metadata = metadata
            // Create directory
            println("Create directory")
            dir.createIfNotExists()
            // Get directory metadata
            println("Get directory metadata")
            metadata = dir.metadata
            val it: MutableIterator<*> = metadata.entries.iterator()
            while (it.hasNext()) {
                val pair = it.next() as Map.Entry<*, *>
                System.out.printf(" %s = %s%n", pair.key, pair.value)
                it.remove()
            }
        } finally { // Delete share
            println("Delete share")
            fileShare.deleteIfExists()
        }
    }

    /**
     * Manage file properties
     * @param fileClient Azure Storage File Service
     */
    @Throws(URISyntaxException::class, StorageException::class)
    private fun fileProperties(fileClient: CloudFileClient) {
        val fileShareName = createRandomName("share-")
        val fileShare = fileClient.getShareReference(fileShareName)
        try { // Create share
            println("Create share")
            fileShare.createIfNotExists()
            // Get the root directory reference of the share
            val rootDir = fileShare.rootDirectoryReference
            // Get a reference to a file
            val file = rootDir.getFileReference("file"
                    + UUID.randomUUID().toString().replace("-", ""))
            // Set file properties
            println("Set file properties")
            file.properties.contentLanguage = "en"
            file.properties.contentType = "text/plain"
            file.properties.contentEncoding = "UTF-8"
            // Create the file
            println("Create file")
            file.create(10)
            // Get file properties
            println("Get file properties")
            val props = file.properties
            println()
            System.out.printf("last modified: %s%n", props.lastModified)
            System.out.printf("cache control: %s%n", props.cacheControl)
            System.out.printf("content type: %s%n", props.contentType)
            System.out.printf("content language: %s%n", props.contentLanguage)
            System.out.printf("content encoding: %s%n", props.contentEncoding)
            System.out.printf("content disposition: %s%n", props.contentDisposition)
            System.out.printf("content MD5: %s%n", props.contentMD5)
            System.out.printf("Length: %s%n", props.length)
            System.out.printf("Copy state: %s%n", props.copyState)
            println()
        } finally { // Delete share
            println("Delete share")
            fileShare.delete()
        }
    }

    /**
     * Manage file metadata
     * @param fileClient Azure Storage File Service
     */
    @Throws(URISyntaxException::class, StorageException::class)
    private fun fileMetadata(fileClient: CloudFileClient) {
        val fileShareName = createRandomName("share-")
        val fileShare = fileClient.getShareReference(fileShareName)
        try { // Create share
            println("Create share")
            fileShare.createIfNotExists()
            // Get the root directory reference of the share
            val rootDir = fileShare.rootDirectoryReference
            // Get a reference to a file
            val file = rootDir.getFileReference("file"
                    + UUID.randomUUID().toString().replace("-", ""))
            // Set file metadata
            println("Set file metadata")
            var metadata = HashMap<String?, String?>()
            metadata["key1"] = "value1"
            metadata["foo"] = "bar"
            file.metadata = metadata
            // Create the file
            println("Create file")
            file.create(10)
            // Get file metadata
            println("Get file metadata:")
            metadata = file.metadata
            val it: MutableIterator<*> = metadata.entries.iterator()
            while (it.hasNext()) {
                val pair = it.next() as Map.Entry<*, *>
                System.out.printf(" %s = %s%n", pair.key, pair.value)
                it.remove()
            }
        } finally { // Delete share
            println("Delete share")
            fileShare.delete()
        }
    }
}