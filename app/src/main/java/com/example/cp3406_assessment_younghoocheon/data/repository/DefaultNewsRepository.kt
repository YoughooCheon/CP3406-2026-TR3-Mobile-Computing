package com.example.cp3406_assessment_younghoocheon.data.repository

import com.example.cp3406_assessment_younghoocheon.data.model.NewsArticle
import com.example.cp3406_assessment_younghoocheon.data.remote.F1NewsApi
import com.example.cp3406_assessment_younghoocheon.data.remote.parseNewsFeed
import com.example.cp3406_assessment_younghoocheon.data.remote.parseOgImage
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.ResponseBody

private const val MAX_ARTICLES = 6

/** Meta tags sit at the top of the page, so reading the first 200 KB is plenty. */
private const val PAGE_HEAD_BYTES = 200_000

/** Safety net: a slow article page must not hold the whole news list back. */
private const val IMAGE_LOOKUP_TIMEOUT_MS = 5_000L

/**
 * [NewsRepository] backed by the official formula1.com RSS feed and article pages.
 *
 * [articleApi] should use an HTTP client with a short call timeout, because a blocking read
 * of a response body cannot be stopped by a coroutine timeout alone.
 */
class DefaultNewsRepository(
    private val feedApi: F1NewsApi,
    private val articleApi: F1NewsApi
) : NewsRepository {

    // IO dispatcher: the article pages are streamed, and streams must not be read on the main thread.
    override suspend fun getLatestNews(): Result<List<NewsArticle>> = withContext(Dispatchers.IO) {
        try {
            val articles = feedApi.getLatestNews()
                .use { body -> parseNewsFeed(body.byteStream()) }
                .take(MAX_ARTICLES)
            Result.success(withImages(articles))
        } catch (e: CancellationException) {
            // Never swallow cancellation, otherwise structured concurrency breaks.
            throw e
        } catch (e: Exception) {
            // Network, HTTP or XML problems: the news card shows one friendly message for all of them.
            Result.failure(e)
        }
    }

    /** Looks up the photo of every article at the same time. Articles without a photo stay in the list. */
    private suspend fun withImages(articles: List<NewsArticle>): List<NewsArticle> = coroutineScope {
        articles
            .map { article -> async { article.copy(imageUrl = findImageUrl(article.link)) } }
            .awaitAll()
    }

    private suspend fun findImageUrl(link: String): String? = try {
        withTimeoutOrNull(IMAGE_LOOKUP_TIMEOUT_MS) {
            articleApi.getArticlePage(link).use { body -> parseOgImage(body.readHead()) }
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        // A missing photo is not an error: the article is still shown, just without a picture.
        null
    }
}

/** Reads at most [PAGE_HEAD_BYTES] bytes of the page as text and stops there. */
private fun ResponseBody.readHead(): String {
    val buffer = ByteArray(PAGE_HEAD_BYTES)
    var total = 0
    byteStream().use { stream ->
        while (total < buffer.size) {
            val read = stream.read(buffer, total, buffer.size - total)
            if (read < 0) break
            total += read
        }
    }
    return String(buffer, 0, total, Charsets.UTF_8)
}