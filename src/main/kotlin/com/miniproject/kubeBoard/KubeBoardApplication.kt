package com.miniproject.kubeBoard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KubeBoardApplication

fun main(args: Array<String>){
    runApplication<KubeBoardApplication>(*args)
}
