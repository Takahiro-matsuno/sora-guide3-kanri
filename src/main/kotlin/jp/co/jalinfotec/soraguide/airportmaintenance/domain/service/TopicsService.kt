package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicsEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.TopicsRepository
import org.springframework.stereotype.Service

@Service
class TopicsService(
        private val topicsRepository: TopicsRepository
) {

    fun getTopicsList(): List<TopicsEntity> {
        return topicsRepository.findByTopicsName("")
    }
}