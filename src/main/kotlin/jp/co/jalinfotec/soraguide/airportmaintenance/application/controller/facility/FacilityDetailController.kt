package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.facility

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.facility.FacilityDbService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam


@Controller
@RequestMapping("/facility/detail")
class FacilityDetailController(
    private val facilityDbService: FacilityDbService
) {

    /**
     * 施設情報詳細画面
     */
    @GetMapping("/get")
    fun getFacilityDetail(
            @RequestParam("id") id: String,
            model: Model
        ): String {

            val facility = facilityDbService.getFacilityData(id.toLong())
            model.addAttribute("facility",facility.get())

        return "facility/facility-detail"
    }


}