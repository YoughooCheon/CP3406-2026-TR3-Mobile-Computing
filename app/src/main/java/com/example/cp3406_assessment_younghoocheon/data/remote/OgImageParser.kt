package com.example.cp3406_assessment_younghoocheon.data.remote

private val META_TAG = Regex("<meta\\s[^>]*>", RegexOption.IGNORE_CASE)
private val CONTENT_ATTRIBUTE = Regex("content\\s*=\\s*\"([^\"]*)\"", RegexOption.IGNORE_CASE)
private val IMAGE_WIDTH = Regex(",w_\\d+/")

private const val OG_IMAGE = "og:image"
private const val OG_IMAGE_SUB_PROPERTY = "og:image:"
private const val WANTED_IMAGE_WIDTH = ",w_900/"

/**
 * Finds the article's main photo in the HTML of an article page.
 *
 * Every formula1.com article declares it in the head as <meta property="og:image" content="...">.
 * Returns null when there is no https address, so the UI can fall back to a placeholder.
 */
fun parseOgImage(html: String): String? {
    for (tag in META_TAG.findAll(html)) {
        val text = tag.value
        val isImageTag = text.contains(OG_IMAGE, ignoreCase = true) &&
                !text.contains(OG_IMAGE_SUB_PROPERTY, ignoreCase = true)
        if (!isImageTag) continue
        val url = CONTENT_ATTRIBUTE.find(text)?.groupValues?.get(1)?.replace("&amp;", "&")
        if (url != null && url.startsWith("https://")) return shrinkImage(url)
    }
    return null
}

/** The page asks for a 2048 px wide photo; a phone card only needs about 900 px. */
private fun shrinkImage(url: String): String = url.replace(IMAGE_WIDTH, WANTED_IMAGE_WIDTH)