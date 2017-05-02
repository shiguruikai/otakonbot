package com.shiguruikai.sample.linebot.vision

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CloudVisionServiceImpl(@Autowired properties: Properties)
    : CloudVisionService by CloudVisionServiceBuilder(properties.apiKey).build()
