package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirportTopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AirportTopicRepository: JpaRepository<AirportTopicEntity,String> {

    fun findByAirportId(airport_id: String) : ArrayList<AirportTopicEntity>
    fun findByTopicId(topic_id: Long) : AirportTopicEntity

}