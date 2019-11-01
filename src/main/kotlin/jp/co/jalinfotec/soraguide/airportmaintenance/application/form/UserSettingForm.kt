package jp.co.jalinfotec.soraguide.airportmaintenance.application.form

data class UserSettingForm(
        val username: String,
        val nowPassword: String = "",
        val newPassword: String = ""
)