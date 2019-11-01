package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.user

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.UserSettingForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

@Controller
class UserController {

    @Autowired
    lateinit var userService: UserService

    /**
     * ホーム画面
     */
    @GetMapping("/home")
    fun getUser(model: Model): String {
        return "user/user-home"
    }

    /**
     * パスワード変更画面
     */
    @GetMapping("/changePassword")
    fun getChangePassword(
            @AuthenticationPrincipal user: User,
            mav: ModelAndView
    ): ModelAndView {

        // アカウント取得
        return if (userService.findByCompanyIdAndUsername(user.getCompanyId(), user.username)) {
            mav.viewName = "user/changePassword"
            mav.addObject("usForm", UserSettingForm(username = user.username))
            mav
        } else {
            // アカウントが見つからない場合はエラー
            // TODO エラー
            mav.viewName = "contents/error"
            //mav.addObject("", "ユーザーが見つかりません。")
            mav
        }
    }

}