package com.example.cp3406_assessment_younghoocheon.data

import com.example.cp3406_assessment_younghoocheon.data.remote.parseOgImage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OgImageParserTest {

    @Test
    fun ogImage_isReturnedAndShrunkToPhoneWidth() {
        val html = """
            <head>
              <meta property="og:title" content="Some title">
              <meta property="og:image" content="https://cdn.example.com/photo,w_2048/race.jpg">
            </head>
        """.trimIndent()

        assertEquals("https://cdn.example.com/photo,w_900/race.jpg", parseOgImage(html))
    }

    @Test
    fun pageWithoutOgImage_returnsNull() {
        val html = """<head><meta property="og:title" content="Some title"></head>"""

        assertNull(parseOgImage(html))
    }

    @Test
    fun ogImageSubProperties_areIgnored() {
        val html = """
            <meta property="og:image:width" content="1200">
            <meta property="og:image:alt" content="https://example.com/not-the-photo.jpg">
            <meta property="og:image" content="https://example.com/photo.jpg">
        """.trimIndent()

        assertEquals("https://example.com/photo.jpg", parseOgImage(html))
    }

    @Test
    fun htmlEntitiesInTheAddress_areDecoded() {
        val html = """<meta property="og:image" content="https://example.com/p.jpg?a=1&amp;b=2">"""

        assertEquals("https://example.com/p.jpg?a=1&b=2", parseOgImage(html))
    }

    @Test
    fun nonHttpsAddress_isRejected() {
        val html = """<meta property="og:image" content="http://example.com/photo.jpg">"""

        assertNull(parseOgImage(html))
    }

    @Test
    fun firstHttpsImageWins_whenEarlierTagsAreUnusable() {
        val html = """
            <meta property="og:image" content="http://example.com/insecure.jpg">
            <meta property="og:image" content="https://example.com/secure.jpg">
        """.trimIndent()

        assertEquals("https://example.com/secure.jpg", parseOgImage(html))
    }

    @Test
    fun tagAndAttributeNames_areCaseInsensitive() {
        val html = """<META PROPERTY="OG:IMAGE" CONTENT="https://example.com/upper.jpg">"""

        assertEquals("https://example.com/upper.jpg", parseOgImage(html))
    }
}