package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.azurestorage

import com.microsoft.azure.storage.StorageException
import com.microsoft.azure.storage.file.*
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.azurestorage.DataGenerator.createRandomName
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.azurestorage.DataGenerator.createTempLocalFile
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.ConnectException
import java.net.URISyntaxException
import java.security.InvalidKeyException
import java.util.*

//----------------------------------------------------------------------------------
// Microsoft Developer & Platform Evangelism
//
// Copyright (c) Microsoft Corporation. All rights reserved.
//
// THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND,
// EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES
// OF MERCHANTABILITY AND/OR FITNESS FOR A PARTICULAR PURPOSE.
//----------------------------------------------------------------------------------
// The example companies, organizations, products, domain names,
// e-mail addresses, logos, people, places, and events depicted
// herein are fictitious.  No association with any real company,
// organization, product, domain name, email address, logo, person,
// places, or events is intended or should be inferred.
//----------------------------------------------------------------------------------


/**
 * This sample illustrates basic usage of the Azure file storage service.
 */
class FileBasics {
    /**
     * Azure Storage File Sample
     *
     * @throws Exception
     */
    @Throws(Exception::class)
    fun runSamples() {
        println("Azure Storage File sample - Starting.")
        var fileClient: CloudFileClient? = null
        var fileShare1: CloudFileShare? = null
        var fileShare2: CloudFileShare? = null
        var fileInputStream: FileInputStream? = null
        try { // Create a file client for interacting with the file service
            fileClient = FileClientProvider.fileClientReference
            // Create sample file for upload demonstration
            val random = Random()
            println("\nCreating sample file between 128KB-256KB in size for upload demonstration.")
            val tempFile1 = createTempLocalFile("file-", ".tmp", 128 * 1024 + random.nextInt(256 * 1024))
            println(String.format("\tSuccessfully created the file \"%s\".", tempFile1!!.absolutePath))
            // Create file share with randomized name
            println("\nCreate file share for the sample demonstration")
            fileShare1 = createFileShare(fileClient, createRandomName("filebasics-"))
            println(String.format("\tSuccessfully created the file share \"%s\".", fileShare1.name))
            // Get a reference to the root directory of the share.
            val rootDir1 = fileShare1.rootDirectoryReference
            // Upload a local file to the root directory
            println("\nUpload the sample file to the root directory.")
            val file1 = rootDir1.getFileReference(tempFile1.name)
            file1.uploadFromFile(tempFile1.absolutePath)
            println("\tSuccessfully uploaded the file.")
            // Create a random directory under the root directory
            println("\nCreate a random directory under the root directory")
            val dir = rootDir1.getDirectoryReference(createRandomName("dir-"))
            if (dir.createIfNotExists()) {
                println(String.format("\tSuccessfully created the directory \"%s\".", dir.name))
            } else {
                throw IllegalStateException(String.format("Directory with name \"%s\" already exists.", dir.name))
            }
            // Upload a local file to the newly created directory sparsely (Only upload certain ranges of the file)
            println("\nUpload the sample file to the newly created directory partially in distinct ranges.")
            val file1sparse = dir.getFileReference(tempFile1.name)
            file1sparse.create(tempFile1.length())
            fileInputStream = FileInputStream(tempFile1)
            println("\t\tRange start: 0, length: 1024.")
            file1sparse.uploadRange(fileInputStream, 0, 1024)
            println("\t\tRange start: 4096, length: 1536.")
            fileInputStream.channel.position(4096)
            file1sparse.uploadRange(fileInputStream, 4096, 1536)
            println("\t\tRange start: 8192, length: EOF.")
            fileInputStream.channel.position(8192)
            file1sparse.uploadRange(fileInputStream, 8192, tempFile1.length() - 8192)
            fileInputStream.close()
            println("\tSuccessfully uploaded the file sparsely.")
            // Query the file ranges
            println(String.format("\nQuery the file ranges for \"%s\".", file1sparse.uri.toURL()))
            var fileRanges = file1sparse.downloadFileRanges()
            run {
                val itr: Iterator<FileRange> = fileRanges.iterator()
                while (itr.hasNext()) {
                    val fileRange = itr.next()
                    println(String.format("\tStart offset: %d, End offset: %d", fileRange.startOffset, fileRange.endOffset))
                }
            }
            // Clear a range and re-query the file ranges
            println(String.format("\nClearing the second range partially and then re-query the file ranges for \"%s\".", file1sparse.uri.toURL()))
            file1sparse.clearRange(4608, 512)
            fileRanges = file1sparse.downloadFileRanges()
            val itr: Iterator<FileRange> = fileRanges.iterator()
            while (itr.hasNext()) {
                val fileRange = itr.next()
                println(String.format("\tStart offset: %d, End offset: %d", fileRange.startOffset, fileRange.endOffset))
            }
            // Create another file share with randomized name
            println("\nCreate another file share for the sample demonstration")
            fileShare2 = createFileShare(fileClient, createRandomName("filebasics-"))
            println(String.format("\tSuccessfully created the file share \"%s\".", fileShare2.name))
            // Get a reference to the root directory of the share.
            val rootDir2 = fileShare2.rootDirectoryReference
            // Create sample file for copy demonstration
            println("\nCreating sample file between 10MB-15MB in size for copy demonstration.")
            val tempFile2 = createTempLocalFile("file-", ".tmp", 10 * 1024 * 1024 + random.nextInt(5 * 1024 * 1024))
            println(String.format("\tSuccessfully created the file \"%s\".", tempFile2!!.absolutePath))
            // Upload a local file to the root directory
            println("\nUpload the sample file to the root directory.")
            val file2 = rootDir1.getFileReference(tempFile2.name)
            file2.uploadFromFile(tempFile2.absolutePath)
            println("\tSuccessfully uploaded the file.")
            // Copy the file between shares
            println(String.format("\nCopying file \"%s\" from share \"%s\" into the share \"%s\".", file2.name, fileShare1.name, fileShare2.name))
            val file2copy = rootDir2.getFileReference(file2.name + "-copy")
            file2copy.startCopy(file2)
            waitForCopyToComplete(file2copy)
            println("\tSuccessfully copied the file.")
            // Abort copying the file between shares
            println(String.format("\nAbort when copying file \"%s\" from share \"%s\" into the share \"%s\".", file2.name, fileShare1.name, fileShare2.name))
            println(String.format("\nAbort when copying file from the root directory \"%s\" into the directory we created \"%s\".", file2.uri.toURL(), dir.uri.toURL()))
            val file2copyaborted = rootDir2.getFileReference(file2.name + "-copyaborted")
            var copyAborted = true
            val copyId = file2copyaborted.startCopy(file2)
            try {
                file2copyaborted.abortCopy(copyId)
            } catch (ex: StorageException) {
                copyAborted = if (ex.errorCode == "NoPendingCopyOperation") {
                    false
                } else {
                    throw ex
                }
            }
            if (copyAborted == true) {
                println("\tSuccessfully aborted copying the file.")
            } else {
                println("\tFailed to abort copying the file because the copy finished before we could abort.")
            }
            // List all file shares and files/directories in each share
            println("\nList all file shares and files/directories in each share.")
            enumerateFileSharesAndContents(fileClient)
            // Download the uploaded files
            println("\nDownload the uploaded files.")
            var downloadedFilePath = String.format("%s%s", System.getProperty("java.io.tmpdir"), file1.name)
            println(String.format("\tDownload the fully uploaded file from \"%s\" to \"%s\".", file1.uri.toURL(), downloadedFilePath))
            file1.downloadToFile(downloadedFilePath)
            File(downloadedFilePath).deleteOnExit()
            downloadedFilePath = String.format("%s%s", System.getProperty("java.io.tmpdir"), file1sparse.name)
            println(String.format("\tDownload the sparsely uploaded file from \"%s\" to \"%s\".", file1sparse.uri.toURL(), downloadedFilePath))
            file1sparse.downloadToFile(downloadedFilePath)
            File(downloadedFilePath).deleteOnExit()
            downloadedFilePath = String.format("%s%s", System.getProperty("java.io.tmpdir"), file2.name)
            println(String.format("\tDownload the copied file from \"%s\" to \"%s\".", file2.uri.toURL(), downloadedFilePath))
            file2.downloadToFile(downloadedFilePath)
            File(downloadedFilePath).deleteOnExit()
            downloadedFilePath = String.format("%s%s", System.getProperty("java.io.tmpdir"), file2copy.name)
            println(String.format("\tDownload the copied file from \"%s\" to \"%s\".", file2copy.uri.toURL(), downloadedFilePath))
            file2copy.downloadToFile(downloadedFilePath)
            File(downloadedFilePath).deleteOnExit()
            downloadedFilePath = String.format("%s%s", System.getProperty("java.io.tmpdir"), file2copyaborted.name)
            println(String.format("\tDownload the copied file from \"%s\" to \"%s\".", file2copyaborted.uri.toURL(), downloadedFilePath))
            file2copyaborted.downloadToFile(downloadedFilePath)
            File(downloadedFilePath).deleteOnExit()
            println("\tSuccessfully downloaded the files.")
            // Delete the files and directory
            print("\nDelete the filess and directory. Press any key to continue...")
            file1.delete()
            file1sparse.delete()
            file2.delete()
            file2copy.delete()
            file2copyaborted.delete()
            println("\tSuccessfully deleted the files.")
            dir.delete()
            println("\tSuccessfully deleted the directory.")
        } catch (t: Throwable) {
            PrintHelper.printException(t)
        } finally { // Delete any file shares that we created (If you do not want to delete the file share comment the line of code below)
            print("\nDelete any file shares we created.")
            if (fileShare1 != null && fileShare1.deleteIfExists() == true) {
                println(String.format("\tSuccessfully deleted the file share: %s", fileShare1.name))
            }
            if (fileShare2 != null && fileShare2.deleteIfExists() == true) {
                println(String.format("\tSuccessfully deleted the file share: %s", fileShare2.name))
            }
            // Close the file input stream of the local temporary file
            fileInputStream?.close()
        }
        println("\nAzure Storage File sample - Completed.\n")
    }

    companion object {
        /**
         * Creates and returns a file share for the sample application to use.
         *
         * @param fileShareName Name of the file share to create
         * @return The newly created CloudFileShare object
         *
         * @throws StorageException
         * @throws RuntimeException
         * @throws IOException
         * @throws URISyntaxException
         * @throws IllegalArgumentException
         * @throws InvalidKeyException
         * @throws IllegalStateException
         */
        @Throws(StorageException::class, RuntimeException::class, IOException::class, InvalidKeyException::class, IllegalArgumentException::class, URISyntaxException::class, IllegalStateException::class)
        private fun createFileShare(fileClient: CloudFileClient?, fileShareName: String): CloudFileShare { // Create a new file share
            val fileShare = fileClient!!.getShareReference(fileShareName)
            try {
                check(fileShare.createIfNotExists() != false) { String.format("File share with name \"%s\" already exists.", fileShareName) }
            } catch (s: StorageException) {
                if (s.cause is ConnectException) {
                    println("Caught connection exception from the client. If running with the default configuration please make sure you have started the storage emulator.")
                }
                throw s
            }
            return fileShare
        }

        /**
         * Enumerates the contents of the file share.
         *
         * @param rootDir Root directory which needs to be enumerated
         *
         * @throws StorageException
         */
        @Throws(StorageException::class)
        private fun enumerateDirectoryContents(rootDir: CloudFileDirectory) {
            val results = rootDir.listFilesAndDirectories()
            val itr: Iterator<ListFileItem> = results.iterator()
            while (itr.hasNext()) {
                val item = itr.next()
                val isDirectory = item.javaClass == CloudFileDirectory::class.java
                println(String.format("\t\t%s: %s", if (isDirectory) "Directory " else "File      ", item.uri.toString()))
                if (isDirectory == true) {
                    enumerateDirectoryContents(item as CloudFileDirectory)
                }
            }
        }

        /**
         * Enumerates the shares and contents of the file shares.
         *
         * @param fileClient CloudFileClient object
         *
         * @throws StorageException
         * @throws URISyntaxException
         */
        @Throws(StorageException::class, URISyntaxException::class)
        private fun enumerateFileSharesAndContents(fileClient: CloudFileClient?) {
            for (share in fileClient!!.listShares("filebasics")) {
                println(String.format("\tFile Share: %s", share.name))
                enumerateDirectoryContents(share.rootDirectoryReference)
            }
        }

        /**
         * Wait until the copy complete.
         *
         * @param file Target of the copy operation
         *
         * @throws InterruptedException
         * @throws StorageException
         */
        @Throws(InterruptedException::class, StorageException::class)
        private fun waitForCopyToComplete(file: CloudFile) {
            var copyStatus = CopyStatus.PENDING
            while (copyStatus == CopyStatus.PENDING) {
                Thread.sleep(1000)
                copyStatus = file.copyState.status
            }
        }
    }
}