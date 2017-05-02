package com.shiguruikai.sample.linebot.vision

data class VisionRequest(val requests: List<AnnotateImageRequest>)

data class AnnotateImageRequest(
        val image: Image,
        val features: List<Feature>)

data class Image(
        val content: String)

data class Feature(
        val type: FeatureType,
        val maxResults: Int = 1)

enum class FeatureType {
    LABEL_DETECTION,
    LOGO_DETECTION,
    TEXT_DETECTION,
    SAFE_SEARCH_DETECTION,
    LANDMARK_DETECTION
}
