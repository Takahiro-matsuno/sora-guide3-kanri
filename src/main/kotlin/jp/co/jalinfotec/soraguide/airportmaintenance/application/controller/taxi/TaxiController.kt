package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.taxi

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/taxi")
class TaxiController {

    /**
     * タクシー管理画面
     */
    @GetMapping("/list")
    fun getTaxiList(): String {
        return "taxi/taxi-list"
    }
}