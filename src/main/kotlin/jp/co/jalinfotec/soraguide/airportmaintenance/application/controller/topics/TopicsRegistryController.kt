package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TopicsForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsFileService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.lang.Exception

@Controller
@RequestMapping("/topics")
class TopicsRegistryController(
        private val topicsFileService: TopicsFileService
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
            @RequestParam("imageFile", required = true) imageFile: MultipartFile,
            @RequestParam("topicUrl", required = true) topicUrl: String,
            model: Model
    ): String {
        return try {
            topicsFileService.uploadTopicsFile(imageFile, topicUrl)
            "topics/topics-reg"
        } catch (ex: Exception) {
            "error"
        }
    }
}