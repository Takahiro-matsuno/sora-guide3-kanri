package jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class User(
        private val user: UserEntity,
        private val authorities: Collection<GrantedAuthority>
) : UserDetails {

    companion object {
        /**
         * https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/core/SpringSecurityCoreVersion.html#SERIAL_VERSION_UID
         * Spring Securityクラスのグローバルシリアライゼーション値。
         * NBクラスは異なるバージョン間で直列化可能であることを意図していません。
         * まだシリアルバージョンが必要な理由については、SEC-1709を参照してください。
         */
        private const val serialVersionUID = -256740067874995659L
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return this.authorities
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return !user.lockFlag
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return user.enableFlg
    }

    fun getCompanyId(): String {
        return user.companyId
    }
}