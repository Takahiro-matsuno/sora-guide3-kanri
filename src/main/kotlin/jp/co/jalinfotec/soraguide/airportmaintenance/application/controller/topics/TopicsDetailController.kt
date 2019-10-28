package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/topics/detail")
class TopicsDetailController {

    /**
     * トピックス詳細画面
     */
    // 画面表示
    @GetMapping("/get")
    fun getDetail(model: Model): String {
        return "topics/topics-detail"
    }

    // 更新
    @PutMapping("/upd")
    fun putDetail(model: Model): String {
        return "topics/topics-list"
    }

    //　削除
    @DeleteMapping("/del")
    fun deleteDetail(model: Model): String {
        return "topics/topics-list"
    }
}