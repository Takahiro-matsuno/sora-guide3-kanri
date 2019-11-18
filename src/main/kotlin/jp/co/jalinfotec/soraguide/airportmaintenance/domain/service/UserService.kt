package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.UserForm
import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.UserEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportCompanyRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.UserRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.invoke.MethodHandles.throwException
import java.util.*


@Service
class UserService(
        private val userRepository: UserRepository,
        private val airportCompanyService: AirportCompanyService,
        private val passwordEncoder: PasswordEncoder,
        private val sendMailService: SendMailService,
        private val airportCompanyRepository: AirportCompanyRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class, LockedException::class)
    override fun loadUserByUsername(username: String?): UserDetails {

        println("UserName:$username")

        if (username == null || username == "") {
            // ユーザー名未入力の場合
            throw UsernameNotFoundException("Username is empty")
        }

        val optional = userRepository.findByUsername(username)
        if (!optional.isPresent) {
            //　ユーザー名が見つからなかった場合
            throw UsernameNotFoundException("User not found: $username")
        }

        val userEntity = optional.get()
        if (!userEntity.enableFlg) {
            throw LockedException("User not allowed: $username")
        }

        return User(userEntity, getAuthorities(userEntity))
    }

    // ユーザー取得
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun findByCompanyIdAndUsername(companyId: String, username: String): Boolean {
        if (!airportCompanyRepository.findById(companyId).isPresent) {
            // 空港会社が見つからない場合は処理終了
            return false
        }
        return true
    }


    // アクセス権限の取得
    private fun getAuthorities(userEntity: UserEntity): Collection<GrantedAuthority> {
        return if (userEntity.adminFlg) {
            AuthorityUtils.createAuthorityList("ROLE_ADMIN", "ROLE_USER")
        } else {
            AuthorityUtils.createAuthorityList("ROLE_USER")
        }
    }

    // ユーザー登録
    @Transactional
    fun registerUser(username: String, password: String): Boolean {

        if(!userRepository.findByUsername(username).isPresent) {
            //ユーザーが存在しない場合
            val user = UserEntity(username = username, password = passwordEncoder.encode(password), adminFlg = false,failureCount =0,lockFlag = false)
            userRepository.save(user)
            return true
        } else {
            return false
        }
    }

    /**
     * パスワード変更
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun changePassword(user: User,usForm: UserForm): Boolean {

        val companyId =user.getCompanyId()

        if(!airportCompanyRepository.findById(companyId).isPresent) {
            // 空港会社が見つからない場合は処理終了
            return false
        }

        // ユーザー取得、見つからない場合は処理終了
        val userRepo =userRepository.findByCompanyIdAndUsername(companyId,user.username) ?: return false

        return if (passwordEncoder.matches(usForm.nowPassword, userRepo.password)) {

            userRepo.password = passwordEncoder.encode(usForm.newPassword)
            userRepository.save(userRepo)
            return true
        } else {
            false
        }
    }



    /**
     * ユーザ情報の取得
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun findByUserName(userName: String): Optional<UserEntity> {
        return userRepository.findByUsername(userName)
    }

    /**
     * ユーザ情報の更新
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun updateAccount(account: UserEntity) {
        userRepository.save(account)
    }


    /**
     * パスワードの初期化
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun resetPassword(userName: String,inputMail: String): Int {
        val account = userRepository.findByUsername(userName)
        if(!account.isPresent) {
            throw Exception()
        }
        val user = account.get()
        val companyInfo = airportCompanyService.getCompanyInfo(user.companyId) ?: throw Exception()

        // メールアドレスの一致確認
        if (inputMail != companyInfo.companyMail) {
            return 1
        }

        // 新パスワード生成
        val newPassword = createPassword()
        println(newPassword)
        user.password =passwordEncoder.encode(newPassword)

        // ログイン失敗回数とアカウントロックのリセット
        user.failureCount = 0
        user.lockFlag = false

        // ユーザ情報の更新
        userRepository.save(user)

        sendMailService.sendAccountResetMail(userName, newPassword, companyInfo.companyName, companyInfo.companyMail)

        return 0
    }

    /**
     * ランダムなパスワード生成処理
     */
    private fun createPassword():String{
        //パスワード桁数
        val length = 8
        //アルファベット大文字小文字のスタイル(normal/lowerCase/upperCase)
        val style = "normal"

        //生成処理
        val result = StringBuilder()
        //パスワードに使用する文字を格納
        val source = StringBuilder()
        //数字
        for (i in 0x30..57) {
            source.append(i.toChar())
        }
        //アルファベット小文字
        when (style) {
            "lowerCase" -> {
            }
            else -> for (i in 0x41..90) {
                source.append(i.toChar())
            }
        }
        //アルファベット大文字
        when (style) {
            "upperCase" -> {
            }
            else -> for (i in 0x61..122) {
                source.append(i.toChar())
            }
        }

        val sourceLength = source.length
        val random = Random()
        while (result.length < length) {
            result.append(source[Math.abs(random.nextInt()) % sourceLength])
        }

        return result.toString()
    }



    /*
    // 管理者追加
    @Transactional
    fun registerAdmin(username: String, password: String) {
        val user = Account(username = username, password = passwordEncoder!!.encode(password), isAdmin = true)
        accRepository!!.save(user)
    }
    */
}