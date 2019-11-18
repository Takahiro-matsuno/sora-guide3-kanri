package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "numbering")
data class NumberingEntity(
        @Id
        @Column(name = "tablename")
        var tableName: String = "",

        @Column(name = "nextvalue")
        var nextValue: Int = 1
)