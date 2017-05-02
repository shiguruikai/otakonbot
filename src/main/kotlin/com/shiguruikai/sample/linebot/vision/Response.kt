package com.shiguruikai.sample.linebot.vision

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.ALWAYS)
data class VisionResponse(val responses: List<AnnotateImageResponse>)

data class AnnotateImageResponse(
        val labelAnnotations: List<LabelAnnotation>?,
        val logoAnnotations: List<LogoAnnotation>?,
        val textAnnotations: List<TextAnnotation>?,
        val landmarkAnnotations: List<LandmarkAnnotation>?,
        val safeSearchAnnotation: SafeSearchAnnotation?
)

data class LabelAnnotation(
        val mid: String?,
        val description: String?,
        val score: Float?)

data class LogoAnnotation(
        val mid: String?,
        val description: String?,
        val score: Float?)

data class TextAnnotation(
        val locale: String?,
        val description: String?,
        val boundingPoly: BoundingPoly?)

data class SafeSearchAnnotation(
        val adult: Likelihood?,
        val spoof: Likelihood?,
        val medical: Likelihood?,
        val violence: Likelihood?)

data class LandmarkAnnotation(
        val mid: String?,
        val description: String?,
        val score: Float?,
        val locations: List<Location>?)

data class Location(
        val latLng: LatLng?)

data class LatLng(
        val latitude: String?,
        val longitude: String?)

data class BoundingPoly(
        val vertices: List<Vertice>?)

data class Vertice(
        val x: Int?,
        val y: Int?)

enum class Likelihood {
    UNKNOWN,
    VERY_UNLIKELY,
    UNLIKELY,
    POSSIBLE,
    LIKELY,
    VERY_LIKELY
}
