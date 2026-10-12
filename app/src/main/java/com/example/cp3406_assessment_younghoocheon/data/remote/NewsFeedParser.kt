package com.example.cp3406_assessment_younghoocheon.data.remote

import android.util.Xml
import com.example.cp3406_assessment_younghoocheon.data.model.NewsArticle
import java.io.InputStream
import org.xmlpull.v1.XmlPullParser

private val WHITESPACE = Regex("\\s+")

/**
 * Reads the RSS feed of formula1.com and returns its <item> entries as [NewsArticle]s.
 *
 * Entries without a title or without an https link are skipped, so the UI never shows
 * a card that cannot be opened. XML problems are thrown to the caller (the repository
 * turns them into a failed Result).
 */
fun parseNewsFeed(input: InputStream): List<NewsArticle> {
    val parser = Xml.newPullParser()
    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
    parser.setInput(input, null)

    val articles = mutableListOf<NewsArticle>()
    var insideItem = false
    var title = ""
    var summary = ""
    var link = ""

    var event = parser.eventType
    while (event != XmlPullParser.END_DOCUMENT) {
        when (event) {
            XmlPullParser.START_TAG -> when (parser.name) {
                "item" -> {
                    insideItem = true
                    title = ""
                    summary = ""
                    link = ""
                }
                // <title> also exists on the channel itself, so only read it inside an <item>.
                "title" -> if (insideItem) title = parser.nextText().clean()
                "description" -> if (insideItem) summary = parser.nextText().clean()
                "link" -> if (insideItem) link = parser.nextText().trim()
            }

            XmlPullParser.END_TAG -> if (parser.name == "item") {
                insideItem = false
                if (title.isNotEmpty() && link.startsWith("https://")) {
                    articles += NewsArticle(title = title, summary = summary, link = link)
                }
            }
        }
        event = parser.next()
    }
    return articles
}

/** Trims the text and collapses line breaks and repeated spaces into single spaces. */
private fun String.clean(): String = trim().replace(WHITESPACE, " ")