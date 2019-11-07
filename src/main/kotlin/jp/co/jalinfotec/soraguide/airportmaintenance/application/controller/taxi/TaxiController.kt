package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.taxi

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.AirportCompanyService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/taxi")
class TaxiController(
        private val airportCompanyService: AirportCompanyService
) {

    /**
     * タクシー管理画面
     */
    @GetMapping("/list")
    fun getTaxiList(
            @AuthenticationPrincipal user: User,
            mav: ModelAndView
    ): String {
        //ユーザーの空港会社
        var companyId = user.getCompanyId()

        //空港とタクシー会社のひも付きテーブルを検索し、タクシー会社IDのリストを取得
       val companyList = airportCompanyService.getCompanyList(companyId)
        //タクシー会社IDのリストからタクシー会社の情報を取得


        //画面を表示
        return "taxi/taxi-list"
    }
}