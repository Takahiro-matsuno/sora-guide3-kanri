package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.company

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TaxiCompanyForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.AirportCompanyService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class CompanyController(

        private val airportCompanyService: AirportCompanyService

) {

    /**
     * 会社情報設定画面表示
     */
    @GetMapping("/company/setting")
    fun getCompanySetting(
            @AuthenticationPrincipal user: User,
            model: Model
    ): String {

        //空港会社ID
        val companyId = user.getCompanyId()

        //会社情報取得
        val airportInfo = airportCompanyService.getCompanyInfo(companyId)

        //会社情報をモデルに設定
        model.addAttribute("airportInfo",airportInfo)

        return "company/company-setting"
    }









}