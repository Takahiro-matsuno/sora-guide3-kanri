package jp.co.jalinfotec.soraguide.airportmaintenance.application.form

import org.springframework.web.multipart.MultipartFile

data class TopicsForm(
        val name: String = "",
        val imageFile: MultipartFile? = null,
        val url: String = "",
        val display: Long = 1
)