package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.login

import org.springframework.context.MessageSource
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import java.util.*

@Controller
class LoginController (
        private val messageSource: MessageSource
 ) {

    /**
     * ログイン画面
     */
    @GetMapping("/login")
    fun getLogin(model: Model): String {
       // println("ログイン画面表示")
        return "login/login"
    }

    /**
     * ログアウト処理
     */
    @GetMapping("/logout")
    fun getLogout(model: Model): String {
        model.addAttribute("message","ログアウトしました。")
        return "login/login"
    }

    /**
     * ログインのPostメソッドはSecurityConfigで受信する
     */
}