package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.facility

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.FacilityEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.FacilityRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList


@Service
class FacilityDbService(
    private val facilityRepository: FacilityRepository

) {
    /**
     * 空港IDに紐づく施設情報をすべて取得する
     */
    fun getFacilityList(airportId: String): ArrayList<FacilityEntity> {
        return facilityRepository.findByAirportIdOrderByShopIdAsc(airportId)
    }

    /**
     * 施設IDに紐づく施設情報を取得する
     */
    fun getFacilityData(shopId: Long): Optional<FacilityEntity> {
        return facilityRepository.findById(shopId)
    }


}