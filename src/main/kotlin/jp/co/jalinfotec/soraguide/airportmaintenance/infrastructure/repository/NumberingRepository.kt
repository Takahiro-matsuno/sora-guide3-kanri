package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.NumberingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NumberingRepository : JpaRepository<NumberingEntity, String>