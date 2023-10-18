package miniproject.kubeBoard.podservice;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class PodServiceApplication

fun main(args: Array<String>){
    runApplication<PodServiceApplication>(*args)
}