package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics

import com.microsoft.azure.storage.CloudStorageAccount
import jp.co.jalinfotec.soraguide.airportmaintenance.util.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.lang.Exception

@Service
class TopicsFileService(
        private val topicsDbService: TopicsDbService
) {

    @Throws(Exception::class)
    fun uploadTopicsFile(imageFile: MultipartFile, url: String) {
        // AZ Storage Client作成
        val storageAccount = CloudStorageAccount.parse(Environment.AZURE_STORAGE_KEY)
        val blobClient = storageAccount.createCloudBlobClient()
        // TODO アカウントによってコンテナを切り替えるようにする
        val container = blobClient.getContainerReference("takamatsu")
        if (container.createIfNotExists()) {
            throw Exception("Container Not Found:${container.name}")
        }
        // 画像をContainerに保存する
        val blob = container.getBlockBlobReference(imageFile.originalFilename)
        blob.uploadFromByteArray(imageFile.bytes, 0, imageFile.size.toInt())
    }

    fun deleteFile() {

    }
}