package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirPortTaxiEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirportEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportCompanyRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportTaxiRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AirportCompanyService(

    private val airportCompanyRepository: AirportCompanyRepository,
    private val airportTaxiRepository: AirportTaxiRepository

) {

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

    //タクシー会社一覧の取得
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


}