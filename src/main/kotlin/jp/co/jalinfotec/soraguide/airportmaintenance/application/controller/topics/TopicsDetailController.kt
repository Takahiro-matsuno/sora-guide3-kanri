package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsDbService
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/topics/detail")
class TopicsDetailController(
        private val topicsService: TopicsDbService
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
    @PutMapping("/upd")
    fun putDetail(model: Model): String {
        return "topics/topics-list"
    }

    //　削除
    @PostMapping("/del")
    fun deleteDetail(
            topic: TopicEntity,
            model: Model
    ): String {



        return "topics/topics-list"
    }
}