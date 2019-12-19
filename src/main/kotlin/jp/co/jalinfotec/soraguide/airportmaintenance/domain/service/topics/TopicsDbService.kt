package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.topics

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TopicsForm
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirportTopicEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TopicEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportTopicRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.TopicRepository
import org.hibernate.exception.JDBCConnectionException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TopicsDbService(
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
     * topicIdから紐づくcompanyIdを取得する
     */
    fun getCompanyId(topicId: Long): String {
        return airportTopicRepository.findByTopicId(topicId).airportId
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

    /**
     * topic内容を取得する
     */
    fun getTopic(topicId: Long): TopicEntity {
        return topicRepository.findById(topicId).get()
    }

    /**
     * topicを登録する
     */
    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun registerTopic(topicsForm: TopicsForm,imageUrl: String): Long {

        val topicEntity = TopicEntity(
                topicName = topicsForm.name,
                topicImage = imageUrl,
                topicUrl = topicsForm.url,
                display = topicsForm.display
        )
        topicRepository.save(topicEntity)


        var topic = topicRepository.findByTopicImage(topicEntity.topicImage)
        return topic.topicId
    }

    /**
     * 空港に紐付くtopicIdを登録する
     */
    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun registerTopicId(companyId: String,topicId: Long) {

        val airportTopicEntity = AirportTopicEntity(
                airportId = companyId,
                topicId = topicId
        )
        airportTopicRepository.save(airportTopicEntity)
    }


    /**
     * topicを削除する
     */
    fun deleteTopic(topicId: Long) {
        //airport_topicテーブルから削除
        val airportTopicEntity = airportTopicRepository.findByTopicId(topicId)
        airportTopicRepository.delete(airportTopicEntity)

        //topicテーブルから削除
        val topicEntity = topicRepository.findByTopicId(topicId)
        topicRepository.delete(topicEntity)
    }

}