package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.taxi

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.AirportCompanyService
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TaxiInformationEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.TaxiCompanyRepository
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/taxi")
class TaxiController(
        private val airportCompanyService: AirportCompanyService,
        private val taxiCompanyRepository: TaxiCompanyRepository
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
        var airportCompanyId = user.getCompanyId()

        //空港とタクシー会社のひも付きテーブルを検索し、タクシー会社IDのリストを取得
        val companyIdList = airportCompanyService.getCompanyList(airportCompanyId)
        var taxiCompanyList = ArrayList<TaxiInformationEntity>()

        //タクシー会社IDのリストからタクシー会社の情報を取得
        if(companyIdList.any()) {
            //タクシー会社IDのリストが取得できた場合のみ
            for(companyId in companyIdList) {
                val company = taxiCompanyRepository.findById(companyId.taxiId)

                if(company.isPresent) {
                   taxiCompanyList.add(company.get())
               }
            }
        }
        mav.addObject("taxiList",taxiCompanyList)

        //画面を表示
        return "taxi/taxi-list"
    }
}