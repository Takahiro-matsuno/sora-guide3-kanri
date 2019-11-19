package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TopicsRepository: JpaRepository<TopicsEntity, String> {
    fun findByCompanyId(companyId: String): ArrayList<TopicsEntity>
}