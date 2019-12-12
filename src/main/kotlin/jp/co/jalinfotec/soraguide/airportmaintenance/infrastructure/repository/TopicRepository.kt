package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TopicRepository: JpaRepository<TopicEntity, Long> {

    fun findByTopicImage(topicImage: String): TopicEntity

}