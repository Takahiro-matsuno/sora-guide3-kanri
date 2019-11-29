package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirportTopicEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportTaxiRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportTopicRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.TopicRepository
import org.springframework.stereotype.Service

@Service
class TopicsService(
        private val topicRepository: TopicRepository,
        private val airportTopicRepository: AirportTopicRepository
) {

    /**
     * companyIdに紐付くtopicIdリストを取得する
     */
    fun getTopicIdList(companyId: String): ArrayList<Long> {
        val airportTopic = airportTopicRepository.findByAirportId(companyId)
        var airportIdList = arrayListOf<Long>()


        airportTopic.forEach {
            airportIdList.add(it.topicId)
        }

        return airportIdList
    }

    /**
     * topicIdリストからtopic内容リストを取得する
     */
    fun getTopicList(topicIdList: ArrayList<Long>): ArrayList<TopicEntity> {

        var topicList = arrayListOf<TopicEntity>()

        topicIdList.forEach{
            topicList.add(topicRepository.findById(it).get())
        }

        return topicList
    }
}