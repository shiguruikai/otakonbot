package com.shiguruikai.sample.linebot.vision

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "google.cloud")
class Properties {

    lateinit var apiKey: String
}
