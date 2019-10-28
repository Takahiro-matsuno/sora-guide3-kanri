package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.login

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView

@Controller
@RequestMapping("/login")
class PasswordResetController {

    @Autowired
    lateinit var userService: UserService

    /**
     * リセット画面
     */
    @GetMapping("/reset")
    fun getReset(model: Model): String {
        return "login/reset"
    }

    @PostMapping("/reset")
    fun doReset(@RequestParam userName: String, @RequestParam("mail") inputMail: String,model: Model): String {
        var result: Int

        try {
            result = userService.resetPassword(userName,inputMail)
        } catch(e: Exception) {
            result = 1
        }

        if(result == 0) {
            model.addAttribute("message","パスワードをリセットしました。メールをご確認ください。")
            return "login/login"
        } else {
            model.addAttribute("errorMessage","ユーザー名または、メールアドレスに誤りがあります。")
            return "login/reset"
        }
    }




}