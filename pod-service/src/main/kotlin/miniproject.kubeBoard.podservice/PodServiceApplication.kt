package miniproject.kubeBoard.podservice;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PodServiceApplication

fun main(args: Array<String>){
    runApplication<PodServiceApplication>(*args)
}