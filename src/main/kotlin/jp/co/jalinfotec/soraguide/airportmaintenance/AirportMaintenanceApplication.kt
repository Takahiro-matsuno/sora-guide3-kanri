package jp.co.jalinfotec.soraguide.airportmaintenance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class AirportMaintenanceApplication: SpringBootServletInitializer()

	fun main(args: Array<String>) {
		runApplication<AirportMaintenanceApplication>(*args)
	}

