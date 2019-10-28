package jp.co.jalinfotec.soraguide.airportmaintenance

import jp.co.jalinfotec.soraguide.airportmaintenance.application.handler.LoginFailureHandler
import jp.co.jalinfotec.soraguide.airportmaintenance.application.handler.LoginSuccessHandler
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

// 参考 http://www.ne.jp/asahi/hishidama/home/tech/java/spring/boot/web/form-auth.html

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter(){

    // UserAccountServiceをコンストラクターで初期化するとエラーになるため、以下に記載
    @Autowired
    private lateinit var userService: UserService

    /**
     * Web全般 Security
     */
    override fun configure(web: WebSecurity) {
        // セキュリティ設定を無視するリクエスト設定
        // 静的リソース(images、css、javascript)に対するアクセスはセキュリティ設定を無視する
        web.ignoring().antMatchers(
                "/images/**",
                "/css/**",
                "/javascript/**",
                "/webjars/**")
    }

    /**
     * HTTP　Security
     */
    override fun configure(http: HttpSecurity) {

        // TODO URLを整理
        http.authorizeRequests()
                .antMatchers(
                        "/login",
                        "/account-lock",
                        "/system-error",
                        "/login/reset",
                        "/login-error"
                ).permitAll() // indexは全ユーザーアクセス許可
                .anyRequest().authenticated()  // それ以外は全て認証無しの場合アクセス不許可

        // ログイン設定
        http.formLogin()
                // ログインページのURL
                .loginPage("/login")
                // ログイン処理URL　[/loginProcess]にPOSTされる
                .loginProcessingUrl("/loginProcess")
                // 認証パラメータ
                .usernameParameter("username")
                .passwordParameter("password")
                // ログイン成功時のハンドラ
                .successHandler(LoginSuccessHandler(userService))
                // ログイン失敗時のハンドラ
                .failureHandler(LoginFailureHandler(userService))
                .permitAll()

        // ログアウト設定
        http.logout()
                // ログアウト処理URL　[/logout]にGET / POSTされる
                .logoutUrl("/logout")
                // ログアウト時URL
                .logoutSuccessUrl("/logout")
                // .deleteCookies().invalidateHttpSession() Cookieの削除
                .permitAll()
    }

    @Autowired
    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        // 認証するユーザーを設定する
        auth.userDetailsService(userService)
                // 入力値をbcryptでハッシュ化した値でパスワード認証を行う
                .passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}