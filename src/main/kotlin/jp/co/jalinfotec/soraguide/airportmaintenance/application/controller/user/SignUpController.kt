package jp.co.jalinfotec.soraguide.airportmaintenance.application.controller.user

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.SignUpForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.UserService
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.UserEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class SignUpController(
    private val userService: UserService

) {


    @GetMapping("/sign-up")
    fun getSignUp(
            @ModelAttribute signUpForm: SignUpForm,
            model: Model
    ): String {
        return "login/sign-up"
    }

    @PostMapping("/sign-up")
    fun postSignUp(
            @ModelAttribute @Validated form: SignUpForm,
            bindingResult: BindingResult,
            model: Model
    ): String {
        return if (bindingResult.hasErrors()) {
            //バリデーションエラーのとき
            println("validation error")
            getSignUp(form, model)
        } else {
            //バリデーション正常のとき
            val success = userService.registerUser(username = form.username, password = form.password)

            if(success) {
                println("Sign-Up Success!!")
            } else {
                println("Sign-Up Error")
            }
            getSignUp(form,model)
        }
    }
}