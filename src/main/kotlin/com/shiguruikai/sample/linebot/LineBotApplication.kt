package com.shiguruikai.sample.linebot

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication


@SpringBootApplication
class LineBotApplication

fun main(args: Array<String>) {
    SpringApplication.run(LineBotApplication::class.java, *args)
}
