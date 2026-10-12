package com.example.cp3406_assessment_younghoocheon.data.model

/**
 * One headline from the official Formula 1 news feed.
 *
 * [imageUrl] is the article's main photo; it is null when the photo could not be found,
 * in which case the UI shows a red-and-black placeholder instead.
 */
data class NewsArticle(
    val title: String,
    val summary: String,
    val link: String,
    val imageUrl: String? = null
)