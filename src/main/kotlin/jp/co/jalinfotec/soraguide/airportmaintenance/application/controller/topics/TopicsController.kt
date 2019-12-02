package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsDbService
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics.TopicsFileService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TopicsController(
        private val topicsService: TopicsDbService,
        private val topicsFileService: TopicsFileService
) {



    /**
     * トピック一覧画面
     */
    @GetMapping("/topics/list")
    fun getTopics(
            @AuthenticationPrincipal user: User,
            model: Model
            ): String {
        //ユーザーの空港IDに紐付くトピック情報を取得し、一覧表示する
        //ユーザーの空港ID
        val companyId = user.getCompanyId()

        //空港に紐付くtopic_idの一覧を取得する
        val topicIdList = topicsService.getTopicIdList(companyId)

        //空港に紐づくtopicがある場合
        if(topicIdList.isNotEmpty()) {
            //取得したtopic_idからtopic内容を取得する
            val topicList = topicsService.getTopicList(topicIdList)

            model.addAttribute("topicList", topicList)
        }

        return "topics/topics-list"
    }

    /**
     * トピック詳細画面
     */
    @GetMapping("/topics/detail")
    fun getTopicDetail(
            @RequestParam("id") id: String,
            model: Model
    ): String {

        val topic = topicsService.getTopic(id.toLong())
        model.addAttribute("topic",topic)
        return "topics/topics-detail"
    }



}