package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.user

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserController {

    @GetMapping("/home")
    fun getUser(model: Model): String {
        return "user/user-home"
    }
}