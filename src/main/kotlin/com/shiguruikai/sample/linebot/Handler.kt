package com.shiguruikai.sample.linebot

import com.linecorp.bot.client.LineMessagingService
import com.linecorp.bot.model.event.MessageEvent
import com.linecorp.bot.model.event.message.ImageMessageContent
import com.linecorp.bot.model.message.Message
import com.linecorp.bot.model.message.TextMessage
import com.linecorp.bot.spring.boot.annotation.EventMapping
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler
import com.shiguruikai.sample.linebot.vision.AnnotateImageRequest
import com.shiguruikai.sample.linebot.vision.CloudVisionService
import com.shiguruikai.sample.linebot.vision.Feature
import com.shiguruikai.sample.linebot.vision.FeatureType.LABEL_DETECTION
import com.shiguruikai.sample.linebot.vision.FeatureType.SAFE_SEARCH_DETECTION
import com.shiguruikai.sample.linebot.vision.Image
import com.shiguruikai.sample.linebot.vision.Likelihood.LIKELY
import com.shiguruikai.sample.linebot.vision.Likelihood.POSSIBLE
import com.shiguruikai.sample.linebot.vision.Likelihood.VERY_LIKELY
import com.shiguruikai.sample.linebot.vision.VisionRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

@LineMessageHandler
class Handler(@Autowired val line: LineMessagingService,
              @Autowired val vision: CloudVisionService) {

    val log: Logger = LoggerFactory.getLogger(Handler::class.java)

    @EventMapping
    fun handleImageMessage(event: MessageEvent<ImageMessageContent>): List<Message> {

        val byteArrayImage = line.getMessageContent(event.message.id).execute().body().bytes()

        val base64Image = Base64.getEncoder().encodeToString(byteArrayImage)

        val request = VisionRequest(
                requests = listOf(AnnotateImageRequest(
                        image = Image(
                                content = base64Image),
                        features = listOf(
                                Feature(type = LABEL_DETECTION),
                                Feature(type = SAFE_SEARCH_DETECTION)))))

        val response = vision.post(request).execute()

        if (!response.isSuccessful) {
            val errorBody = response.errorBody().string()
            log.error(errorBody)
            return listOf(TextMessage(errorBody))
        }

        val annotateImageResponse = response.body().responses.first()

        val adult = annotateImageResponse.safeSearchAnnotation?.adult
        val violence = annotateImageResponse.safeSearchAnnotation?.violence
        val spoof = annotateImageResponse.safeSearchAnnotation?.spoof

        when (adult) {
            VERY_LIKELY, LIKELY -> {
                return listOf(
                        TextMessage("また、こんなの撮って。"),
                        TextMessage("やっぱり君は……。"),
                        TextMessage("……保存しとこ。"))
            }
            POSSIBLE -> {
                return listOf(
                        TextMessage("うぉ！"),
                        TextMessage("こ、これは……！！"),
                        TextMessage("……保存しとこ。"))
            }
            else -> Unit
        }

        when (violence) {
            VERY_LIKELY -> {
                return listOf(
                        TextMessage("もうやめてくれ！"),
                        TextMessage("嫌がらせか！？"))
            }
            LIKELY, POSSIBLE -> {
                return listOf(
                        TextMessage("…君、こういう趣味があったんだ……。"))
            }
            else -> Unit
        }

        when (spoof) {
            VERY_LIKELY, LIKELY, POSSIBLE -> {
                return listOf(
                        TextMessage("うははははは！"),
                        TextMessage("何だこりゃ！"),
                        TextMessage("サイコー！！"))
            }
            else -> Unit
        }

        val description = annotateImageResponse.labelAnnotations?.first()?.description

        return if (description != null) {
            listOf(TextMessage(description + "の写真だね。"))
        } else {
            listOf(TextMessage("写真を撮り直してもう一度送ってくれ。"))
        }
    }
}
