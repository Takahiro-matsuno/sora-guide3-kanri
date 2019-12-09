package jp.co.jalinfotec.soraguide.airportmaintenance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class BootApp: SpringBootServletInitializer()