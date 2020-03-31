package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.FacilityEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FacilityRepository: JpaRepository<FacilityEntity, Long> {

    fun findByAirportIdOrderByShopIdAsc(airportId: String): ArrayList<FacilityEntity>

}