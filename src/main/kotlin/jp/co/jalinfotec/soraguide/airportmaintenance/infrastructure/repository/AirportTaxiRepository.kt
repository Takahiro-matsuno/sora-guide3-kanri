package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirPortTaxiEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AirportTaxiRepository: JpaRepository<AirPortTaxiEntity,String> {

    fun findByAirportId(airport_id: String) : MutableList<AirPortTaxiEntity>
    fun findByTaxiId(taxi_id: String) :  MutableList<AirPortTaxiEntity>
}