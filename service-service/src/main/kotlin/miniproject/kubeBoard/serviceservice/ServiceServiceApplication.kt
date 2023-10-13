package miniproject.kubeBoard.serviceservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class ServiceServiceApplication

fun main(args: Array<String>) {
	runApplication<ServiceServiceApplication>(*args)
}
