package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TopicsForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsDbService
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsFileService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception

@Controller
@RequestMapping("/topics")
class TopicsRegistryController(
        private val topicsFileService: TopicsFileService,
        private val topicsDbService: TopicsDbService
) {

    /**
     * トピックス登録
     */
    @GetMapping("/reg")
    fun getUpload(
            @ModelAttribute topicsForm:TopicsForm,
            model: Model
    ): String {

        return "topics/topics-reg"
    }

    @PostMapping("/reg")
    fun postUpload(
            @AuthenticationPrincipal user: User,
            @Validated topicsForm: TopicsForm,
           // @RequestParam("imageFile", required = true) imageFile: MultipartFile,
            bindingResult: BindingResult,
            model: Model
    ): String {
        return try {

            println("コントローラー開始")

            //topicsForm.imageFile = imageFile

            //TODO これでええんか・・・
            if(topicsForm.imageFile == null || bindingResult.hasErrors()) {
                throw Exception()
            }
            // 画像ファイルをアップロード
            val imageUrl = topicsFileService.uploadTopicsFile(topicsForm.imageFile!!, user.getCompanyId())
            // topicテーブルを更新
            topicsDbService.registerTopic(topicsForm, imageUrl)

            //airport_topicテーブルを更新
           // topicsDbService.registerTopicId(companyId,)
            "topics/topics-reg"
        } catch (ex: Exception) {
            ex.printStackTrace()
            "error"
        }
    }
}