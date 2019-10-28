package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import javax.persistence.*

@Entity
@Table(name ="hoge_user_info")
data class UserEntity(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "user_id", nullable = false, unique = true)
        var userId: Long = -1,

        @Column(name = "user_name", nullable = false, unique = true)
        var username: String = "",

        @Column(nullable = false)
        var password: String = "",

        @Column(name = "enable_flg", nullable = false)
        var enableFlg: Boolean = false,

        @Column(name = "adminFlg", nullable = false)
        val adminFlg: Boolean = false,

        @Column(name = "companyId", nullable = false)
        val companyId: String = "",

        @Column(name = "failure_count", nullable = false)
        var failureCount: Int = -1,

        @Column(name = "lock_flag", nullable = false)
        var lockFlag: Boolean = false
)