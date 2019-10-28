package jp.co.jalinfotec.soraguide.airportmaintenance.application.handler

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.UserService
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.WebAttributes
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginFailureHandler(private val userAccountService: UserService) : AuthenticationFailureHandler {

    companion object {
        // アカウントロックまでの連続ログイン失敗回数閾値
        const val ACCOUNT_LOCK = 30
    }

    @Throws(IOException::class, ServletException::class)
    override fun onAuthenticationFailure(request: HttpServletRequest,
                                         response: HttpServletResponse,
                                         ex: AuthenticationException) {
        //val telemetry = TelemetryClient()

        // エラー種別による処理の区別
        if (ex is BadCredentialsException) {
            //ユーザ名、またはパスワード誤りの場合

            //入力されたユーザ名を取得
            val userName = request.getParameter("username")
            println(userName)
            // telemetry.trackTrace("username:"+ userName+",Password:"+request.getParameter("password"))
            //DBからユーザ情報を取得
            val account = userAccountService.findByUserName(userName)

            if (account.isPresent) {
                var user = account.get()
                //ユーザが存在する場合、失敗回数を加算
                println("失敗回数加算")
                user.failureCount++

                if (user.failureCount >= ACCOUNT_LOCK) {
                    //ログイン失敗回数が閾値を超えている場合、アカウントロック
                    println("アカウントロック")
                    user.lockFlag = true
                }
                //ユーザ情報の更新
                userAccountService.updateAccount(user)
            }
        }

        request.session.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, ex)
        response.sendRedirect("./login")
    }
}