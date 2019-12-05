package jp.co.jalinfotec.soraguide.airportmaintenance.application.form

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class TopicsForm(

        @get:NotEmpty
        val name: String = "",
        val imageFile: MultipartFile? = null,
        @get:NotEmpty
        val url: String = "",
        val display: Long = 1
)