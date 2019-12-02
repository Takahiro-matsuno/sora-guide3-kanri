package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.azurestorage

import com.microsoft.azure.storage.CloudStorageAccount
import com.microsoft.azure.storage.file.CloudFileClient
import jp.co.jalinfotec.soraguide.airportmaintenance.util.Environment
import java.io.IOException
import java.net.URISyntaxException
import java.security.InvalidKeyException
import java.util.*

/**
 * Manages the storage file client
 */
internal object FileClientProvider {// Retrieve the connection string
    /**
     * Validates the connection string and returns the storage file client.
     * The connection string must be in the Azure connection string format.
     *
     * @return The newly created CloudFileClient object
     */
    @get:Throws(RuntimeException::class, IOException::class, URISyntaxException::class, InvalidKeyException::class)
    val fileClientReference: CloudFileClient
        get() { // Retrieve the connection string
            val prop = Properties()
            try {
                val propertyStream = FileBasics::class.java.classLoader.getResourceAsStream("application.properties")
                if (propertyStream != null) {
                    prop.load(propertyStream)
                } else {
                    throw RuntimeException()
                }
            } catch (e: RuntimeException) {
                println("\nFailed to load config.properties file.")
                throw e
            } catch (e: IOException) {
                println("\nFailed to load config.properties file.")
                throw e
            }
            val storageAccount: CloudStorageAccount
            storageAccount = try {
                CloudStorageAccount.parse(Environment.AZURE_STORAGE_KEY)
            } catch (e: IllegalArgumentException) {
                println("\nConnection string specifies an invalid URI.")
                println("Please confirm the connection string is in the Azure connection string format.")
                throw e
            } catch (e: URISyntaxException) {
                println("\nConnection string specifies an invalid URI.")
                println("Please confirm the connection string is in the Azure connection string format.")
                throw e
            } catch (e: InvalidKeyException) {
                println("\nConnection string specifies an invalid key.")
                println("Please confirm the AccountName and AccountKey in the connection string are valid.")
                throw e
            }
            return storageAccount.createCloudFileClient()
        }
}