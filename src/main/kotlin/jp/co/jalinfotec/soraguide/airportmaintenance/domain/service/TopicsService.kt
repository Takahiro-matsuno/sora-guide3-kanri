package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.TopicRepository
import org.springframework.stereotype.Service

@Service
class TopicsService(
        private val topicsRepository: TopicRepository
) {

    fun getTopicsList(companyId: String): ArrayList<TopicEntity> {
        return topicsRepository.findByCompanyId(companyId)
    }
}