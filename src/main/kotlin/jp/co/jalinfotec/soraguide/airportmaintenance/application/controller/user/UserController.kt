package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.user

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.UserForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
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
     * パスワード変更画面表示
     */
    @GetMapping("/changePassword")
    fun getChangePassword(
            @AuthenticationPrincipal user: User,
            mav: ModelAndView
    ): ModelAndView {

        // アカウント取得
        return if (userService.findByCompanyIdAndUsername(user.getCompanyId(), user.username)) {
            mav.viewName = "user/changePassword"
            mav.addObject("usForm", UserForm(username = user.username))
            mav
        } else {
            // アカウントが見つからない場合はエラー
            // TODO エラー
            mav.viewName = "contents/error"
            //mav.addObject("", "ユーザーが見つかりません。")
            mav
        }
    }

    /**
     * パスワード変更処理
     */
    @PostMapping("/changePassword")
    fun changePassword(
            @AuthenticationPrincipal user:User,
            @ModelAttribute(value = "usForm") usForm: UserForm,
            mav: ModelAndView
            ): ModelAndView {

        //ユーザーチェック
        if (user.username != usForm.username) {
            // ユーザー名が一致しない場合はエラー
            mav.viewName = "login/login"
            mav.addObject("message", "再度ログインしてください。")

            return mav
        }

        // ユーザー情報更新処理
        mav.viewName = "user/changePassword"
        if(userService.changePassword(user,usForm)) {
            //更新成功
            mav.addObject("message", "ユーザ情報を更新しました。")
            mav.addObject("usForm", UserForm(username = user.username))
        } else {
            //更新失敗
            mav.addObject("message", "更新に失敗しました。")
        }
        return mav

    }

}