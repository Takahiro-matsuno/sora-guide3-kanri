package jp.co.jalinfotec.soraguide.airportmaintenance.application.form

import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class TopicsForm(

        @get:NotEmpty
        var name: String = "",
        var imageFile: MultipartFile? = null,
        @get:NotEmpty
        var url: String = "",
        var display: Long = 1
)