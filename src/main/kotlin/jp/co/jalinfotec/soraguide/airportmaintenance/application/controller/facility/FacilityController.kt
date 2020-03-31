package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.facility

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.facility.FacilityDbService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class FacilityController(
    private val facilityDbService: FacilityDbService
) {

    /**
     * 施設情報一覧画面
     */
    @GetMapping("/facility/list")
    fun getFacility(
            @AuthenticationPrincipal user: User,
            model: Model
            ): String {
        //空港IDに紐づく施設情報を取得する
        val facilityList = facilityDbService.getFacilityList(user.getCompanyId())
        model.addAttribute("facilityList",facilityList)

        return "facility/facility-list"
    }





}

