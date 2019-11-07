package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import javax.persistence.*

@Entity
@Table(name ="airport_taxi")
data class AirPortTaxiEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id", nullable = false, unique = true)
        var id: Long = 0L,

        @Column(name = "airport_id", nullable = false, unique = false)
        val airportId: String = "",

        @Column(name = "taxi_id", nullable = false, unique = false)
        val taxiId: String = ""
)