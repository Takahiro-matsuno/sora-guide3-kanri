package jp.co.jalinfotec.soraguide.airportmaintenance.domain.service

import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.entity.AirportEntity
import jp.co.jalinfotec.soraguide.airportmaintenance.infrastructure.repository.AirportCompanyRepository
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AirportCompanyService(

    private val airportCompanyRepository: AirportCompanyRepository

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

}