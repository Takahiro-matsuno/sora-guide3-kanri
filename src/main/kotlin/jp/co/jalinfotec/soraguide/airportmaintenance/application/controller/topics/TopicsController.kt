package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.TopicsService
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TaxiInformationEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicsEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class TopicsController(
        private val topicsService: TopicsService
) {



    /**
     * トピックス一覧画面
     */
    @GetMapping("/topics/list")
    fun getTopics(
            @AuthenticationPrincipal user: User,
            model: Model
            ): String {
        //ユーザーの空港IDに紐付くトピック情報を取得し、一覧表示する
        //ユーザーの空港ID
        var companyId = user.getCompanyId()


        var topicsList = topicsService.getTopicsList(companyId)


        model.addAttribute("topicsList",topicsList)

        return "topics/topics-list"
    }
}