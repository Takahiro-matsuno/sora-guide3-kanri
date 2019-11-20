package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.domain.`object`.User
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirPortTaxiEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirportEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportCompanyRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportTaxiRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.UserRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AirportCompanyService(

    private val airportCompanyRepository: AirportCompanyRepository,
    private val airportTaxiRepository: AirportTaxiRepository,
    private val userRepository: UserRepository

) {

    /**
     * 空港会社情報の取得
     */
    @Transactional
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getCompanyInfo(companyId: String): AirportEntity? {
        val airportInfoOptional = airportCompanyRepository.findById(companyId)

        return if(airportInfoOptional.isPresent) {
            airportInfoOptional.get()
        }else {
            null
        }
    }

    /**
     * タクシー会社一覧の取得
     */
    @Transactional(readOnly = true)
    @Retryable(value = [Exception::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun getCompanyList(airportId: String): ArrayList<AirPortTaxiEntity> {

        val taxiList = ArrayList<AirPortTaxiEntity>()

        if (!airportCompanyRepository.findById(airportId).isPresent) {
            // タクシー会社が見つからない場合は処理終了
            return taxiList
        }

        //空港に紐づくタクシー会社一覧を取得
         val result = airportTaxiRepository.findByAirportId(airportId)

        //詰め替え
        for(taxi in result) {
            taxiList.add(taxi)
        }

        return taxiList
    }

    /**
     * 空港会社情報の更新
     */
    fun updateAirportInformation(airportInfo: AirportEntity,user: User): Boolean {

        val acc = userRepository.findByCompanyIdAndUsername(user.getCompanyId(),user.username) ?: return false
        val optional = airportCompanyRepository.findById(acc.companyId)


        if (!optional.isPresent) {
            // 会社情報が見つからない場合は終了
            return false
        }
        val company = optional.get()

        company.companyName = airportInfo.companyName
        company.contact = airportInfo.contact
        airportCompanyRepository.save(company)

        return true
    }
}