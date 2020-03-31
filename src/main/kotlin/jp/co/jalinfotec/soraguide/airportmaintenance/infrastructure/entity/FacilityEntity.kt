package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import javax.persistence.*

@Entity
@Table(name = "shop_info")
data class FacilityEntity (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "shop_id", nullable = false, unique = true)
        val shopId: Long = -1,

        @Column(name = "shop_title", nullable = false, unique = false)
        val shopTitle: String = "",

        @Column(name = "shop_image", nullable = true, unique = false)
        val shopImage: String = "",

        @Column(name = "opening_hours", nullable = true, unique = false)
        val openingHours: String = "",

        @Column(name = "shop_category", nullable = true, unique = false)
        val shopCategory: String = "",

        @Column(name = "shop_tel", nullable = true, unique = false)
        val shopTel: String = "",

        @Column(name = "shop_text", nullable = true, unique = false)
        val shopText: String = "",

        @Column(name = "shop_location", nullable = true, unique = false)
        val shopLocation: String = "",

        @Column(name = "airport_id", nullable = false, unique = false)
        val airportId: String = ""
)