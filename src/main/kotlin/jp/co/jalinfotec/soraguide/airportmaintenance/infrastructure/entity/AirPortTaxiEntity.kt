package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import java.math.BigInteger
import javax.persistence.*

@Entity
@Table(name ="airport_taxi")
data class AirPortTaxiEntity(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "airport_taxi_id", nullable = false, unique = true)
        var airport_taxi_id: Long = -1,

        @Column(name = "airport_id", nullable = false, unique = false)
        val airportId: String = "",

        @Column(name = "taxi_id", nullable = false, unique = false)
        val taxiId: String = ""
)