package jp.co.jalinfotec.soraguide.airportmaintenance.application.form

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern

data class SignUpForm(
        @get:NotEmpty
        @get:Pattern(regexp="[a-zA-Z0-9]*")
        val username: String="",
        @get:NotEmpty
        val password: String=""
)