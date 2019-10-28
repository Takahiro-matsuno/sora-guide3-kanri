package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class TopicsController {

    /**
     * トピックス一覧画面
     */
    // 画面表示
    @RequestMapping("/topics/list")
    fun getTopics(model: Model): String {
        return "topics/topics-list"
    }
}