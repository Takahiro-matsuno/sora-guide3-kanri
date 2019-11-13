package jp.co.jalinfotec.soraguide.airportmaintenance.application.form

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class TaxiCompanyForm(
        @get:NotEmpty
        @get:Size(min = 1, max = 30)
        var companyName: String ="",

        @get:NotEmpty
        @get:Size(min = 1, max = 15)
        @get:Pattern(regexp = "[0-9]*")
        var companyContact: String ="",

        @get:NotEmpty
        @Email
        @get:Size(min = 1, max = 50)
        var companyMail: String=""
)