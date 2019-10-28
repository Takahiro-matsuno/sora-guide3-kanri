package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.ad

import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/ad")
class AdController {

    /**
     * 広告一覧画面
     */
    @GetMapping("/list")
    fun getAd(): String {
        return ""
    }

}