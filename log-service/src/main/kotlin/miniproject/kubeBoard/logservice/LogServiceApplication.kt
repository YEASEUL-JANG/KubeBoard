package miniproject.kubeBoard.logservice;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class LogServiceApplication

fun main(args: Array<String>){
    runApplication<LogServiceApplication>(*args)
}