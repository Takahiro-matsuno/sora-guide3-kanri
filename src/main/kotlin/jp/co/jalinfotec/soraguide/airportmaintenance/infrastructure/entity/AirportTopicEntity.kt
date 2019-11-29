package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import javax.persistence.*

@Entity
@Table(name = "airport_topic")
data class AirportTopicEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "airport_topic_id", nullable = false, unique = true)
        val airportTopicId: Long = -1,

        @Column(name = "airport_id", nullable = false, unique = false)
        val airportId: String = "",

        @Column(name = "topic_id", nullable = false, unique = false)
        val topicId: Long = -1

)
