package jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "airport_info")
class AirportEntity(
        @Id
        @Column(name = "company_id")
        var companyId: String = "",

        @Column(name = "company_name")
        var companyName: String = "",

        @Column(name = "contact")
        var contact: String = "",

        @Column(name = "company_mail")
        var companyMail: String = ""
)