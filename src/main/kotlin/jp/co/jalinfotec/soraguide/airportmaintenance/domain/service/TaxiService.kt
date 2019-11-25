package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.application.form.TaxiCompanyForm
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirPortTaxiEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.TaxiInformationEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportTaxiRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.NumberingRepository
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.TaxiCompanyRepository
import org.hibernate.exception.JDBCConnectionException
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger


@Service
class TaxiService(

        private val taxiCompanyRepository: TaxiCompanyRepository,
        private val numberingRepository: NumberingRepository,
        private val airportTaxiRepository: AirportTaxiRepository

) {


    /**
     * タクシー会社登録
     */
    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun registerTaxi(taxiCompanyForm: TaxiCompanyForm,airportId: String): Boolean {

        //TODO 登録時のチェックを入れる(同じ会社名はNG等)


       val taxi = TaxiInformationEntity(
               companyId = getNewCompanyId(),
               companyName = taxiCompanyForm.companyName,
               contact = taxiCompanyForm.companyContact,
               companyMail = taxiCompanyForm.companyMail
       )
        taxiCompanyRepository.save(taxi)

        //空港とタクシー会社のひも付きテーブル(airport_taxi)への追加処理
        addAirportTaxi(airportId,taxi.companyId)


        return true
    }

    /**
     * 次のタクシー会社IDを取得
     */
    fun getNewCompanyId():String {

        val numbering = numberingRepository.findById("taxi_info").get()

        // 予約番号を4桁にゼロパディング
        val result = String.format("%04d", numbering.nextValue)

        // 予約番号の値を更新
        numbering.nextValue++
        numberingRepository.save(numbering)

        return result
    }

    /**
     * 空港会社IDとタクシー会社IDの紐付きテーブルに追加する
     */
    @Transactional
    @Retryable(value = [JDBCConnectionException::class], maxAttempts = 3, backoff = Backoff(delay = 1000))
    fun addAirportTaxi(airportId: String,taxiCompanyId: String) {
        val airportTaxi = AirPortTaxiEntity(
               // id = getNewAirPortTaxiId(),
                airportId = airportId,
                taxiId = taxiCompanyId
        )
        airportTaxiRepository.save(airportTaxi)
    }

    /**
     * airport_taxiの次のIdを取得
     */
    fun getNewAirPortTaxiId(): BigInteger {
        val numbering = numberingRepository.findById("airport_taxi").get()
        val result = numbering.nextValue

        numbering.nextValue++
        numberingRepository.save(numbering)

        return result.toBigInteger()
    }

    /**
     * タクシー会社を削除する
     */
    fun deleteTaxi(companyId: String) {
        //タクシー会社テーブルから削除
        //taxiCompanyRepository.deleteById(companyId)

        //airport_taxiテーブルから削除対象のairport_taxi_idを取得する
        val airportTaxiList = airportTaxiRepository.findByTaxiId(companyId)

        //airport_taxiテーブルから削除
        airportTaxiList.forEach(){
            airportTaxiRepository.delete(it)
        }

    }



}