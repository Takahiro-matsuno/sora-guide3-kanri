package jp.co.jalinfotec.soraguide.airportmaintenance.application.handler

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginSuccessHandler(
        private val userAccountService: UserService
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse, authentication: Authentication) {
        //入力されたユーザ名を取得
        val userName = request.getParameter("username")
        println(userName)

        //DBからユーザ情報を取得
        val account = userAccountService.findByUserName(userName)
        var user = account.get()
        user.failureCount = 0

        userAccountService.updateAccount(user)
//        val telemetry = TelemetryClient()
//        telemetry.trackEvent("login_successful")
//        telemetry.trackTrace("username:"+ userName+",Password:"+request.getParameter("password"))

        response.sendRedirect("./home")
    }
}