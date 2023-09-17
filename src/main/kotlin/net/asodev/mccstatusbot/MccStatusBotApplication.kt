package net.asodev.mccstatusbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MccStatusBotApplication

fun main(args: Array<String>) {
    runApplication<MccStatusBotApplication>(*args)
}
