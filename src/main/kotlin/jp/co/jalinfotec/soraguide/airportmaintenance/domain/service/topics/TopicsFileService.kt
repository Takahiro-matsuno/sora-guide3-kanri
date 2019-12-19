package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics

import com.microsoft.azure.storage.CloudStorageAccount
import jp.co.jalinfotec.soraguide.airportmaintenance.util.Environment
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception

@Service
class TopicsFileService {

    @Throws(Exception::class)
    fun uploadTopicsFile(imageFile: MultipartFile, companyId: String): String {
        // AZ Storage Client作成
        val storageAccount = CloudStorageAccount.parse(Environment.AZURE_STORAGE_KEY)
        val blobClient = storageAccount.createCloudBlobClient()
        // 空港IDごとに別コンテナに格納する
        val container = blobClient.getContainerReference(companyId)
        if (container.createIfNotExists()) {
            /**
             * TODO 以下を修正する
             *  1. コンテナ自動作成時にPrivateになる
             *  2. コンテナ自動作成時にNotExistsに入る
             */
            throw Exception("Container Not Found:${container.name}")
        }
        // 画像をContainerに保存する
        val blob = container.getBlockBlobReference(imageFile.originalFilename)
        blob.uploadFromByteArray(imageFile.bytes, 0, imageFile.size.toInt())
        blob.properties.contentType = imageFile.contentType
        blob.uploadProperties()

        return blob.uri.toString()
    }

    /**
     * ファイルの削除
     * arg1:ファイル名,arg2:空港会社ID
     */
    fun deleteFile(fileName:String,companyId: String) {
        // AZ Storage Client作成
        val storageAccount = CloudStorageAccount.parse(Environment.AZURE_STORAGE_KEY)
        val blobClient = storageAccount.createCloudBlobClient()
        //コンテナーを取得
        val container = blobClient.getContainerReference(companyId)

        //コンテナー内のファイルを削除
        val blob = container.getBlockBlobReference(fileName)
        blob.delete()
    }
}