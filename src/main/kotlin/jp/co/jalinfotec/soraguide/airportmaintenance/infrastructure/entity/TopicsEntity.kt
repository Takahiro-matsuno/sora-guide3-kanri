package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import javax.persistence.*

@Entity
@Table(name = "hoge_topics_info")
data class TopicsEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "topics_id", nullable = false, unique = true)
        val topicsId: Long = -1,

        @Column(name = "topics_name", nullable = false, unique = true)
        val topicsName: String = "",

        @Column(name = "company_id", nullable = false, unique = false)
        val companyId: String = ""
)