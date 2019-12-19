package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TopicsForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsDbService
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsFileService
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/topics/detail")
class TopicsDetailController(
        private val topicsService: TopicsDbService,
        private val topicsFileService: TopicsFileService
) {

    /**
     * トピックス詳細画面
     */
    // 画面表示
    @GetMapping("/get")
    fun getDetail(
            @RequestParam("id") id: String,
            model: Model
    ): String {

        val topic = topicsService.getTopic(id.toLong())
        model.addAttribute("topic",topic)
        return "topics/topics-detail"
    }


    // 更新
    @PostMapping("/upd")
    fun putDetail(
            topic: TopicEntity,
            model: Model
    ): String {

        //送られてきたEntityで更新を行う
        //画像は更新不可とする
        //画像を変えたい場合は、削除→新規で追加してもらう






        return "user/user-home"
    }

    //　削除
    @PostMapping("/delete")
    fun deleteDetail(
            @AuthenticationPrincipal user: User,
            topic: TopicEntity,
            model: Model
    ): String {
        val companyId = user.getCompanyId()
        val imageUrl = topic.topicImage
        val array = imageUrl.split("/")
        val filename = array[array.size -1]

        //対象のtopicIdがcompanyIdに紐付いているかチェック
        if(!companyId.equals(topicsService.getCompanyId(topic.topicId))) {
            return "user/user-home"
        }


        //DBからtopicを削除
        topicsService.deleteTopic(topic.topicId)

        //Azureストレージから対象ファイルを削除
        topicsFileService.deleteFile(filename,companyId)

        return "user/user-home"
    }
}