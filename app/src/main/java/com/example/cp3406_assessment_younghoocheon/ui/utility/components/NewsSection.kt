package com.example.cp3406_assessment_younghoocheon.ui.utility.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cp3406_assessment_younghoocheon.R
import com.example.cp3406_assessment_younghoocheon.ui.preview.PreviewData
import com.example.cp3406_assessment_younghoocheon.ui.theme.AppTheme
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1CardBorder
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1CountdownEnd
import com.example.cp3406_assessment_younghoocheon.ui.theme.F1CountdownStart
import com.example.cp3406_assessment_younghoocheon.ui.utility.NewsUiState
import com.example.cp3406_assessment_younghoocheon.data.model.NewsArticle

private const val NEWS_HOME_URL = "https://www.formula1.com/en/latest"
private const val FEATURED_SUMMARY_LINES = 3
private const val TITLE_LINES = 2
private const val FEATURED_TITLE_LINES = 3
private const val PHOTO_RATIO = 16f / 9f
private const val SCRIM_ALPHA = 0.85f

/**
 * "Latest F1 news" block of the home screen: one big top story with a photo, then compact headlines.
 * Tapping a story opens the full article on formula1.com in the browser.
 */
@Composable
fun NewsSection(
    state: NewsUiState,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NewsSectionTitle()
        when (state) {
            is NewsUiState.Loading -> NewsLoading()
            is NewsUiState.Error -> NewsError(onRetry = onRetry)
            is NewsUiState.Success -> NewsList(articles = state.articles)
        }
    }
}

/** Red accent bar followed by the section name, like the headings on formula1.com. */
@Composable
private fun NewsSectionTitle() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(22.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = stringResource(R.string.news_title),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
private fun NewsList(articles: List<NewsArticle>) {
    if (articles.isEmpty()) {
        NewsMessage(text = stringResource(R.string.news_empty))
        return
    }
    val uriHandler = LocalUriHandler.current
    FeaturedNewsCard(article = articles.first())
    if (articles.size > 1) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = F1CardBorder
        ) {
            articles.drop(1).forEachIndexed { index, article ->
                if (index > 0) HorizontalDivider()
                NewsHeadlineRow(article = article)
            }
        }
    }
    TextButton(onClick = { uriHandler.openUri(NEWS_HOME_URL) }) {
        Text(text = stringResource(R.string.news_more))
    }
}

/**
 * The first story: a big photo with a dark fade at the bottom, the red "TOP STORY" tag and the
 * title written on top of it, and the summary underneath. Without a photo the area is filled
 * with the same red-to-black gradient as the countdown card.
 */
@Composable
private fun FeaturedNewsCard(article: NewsArticle) {
    val uriHandler = LocalUriHandler.current
    Card(
        onClick = { uriHandler.openUri(article.link) },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        ),
        border = F1CardBorder
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(PHOTO_RATIO)
                .background(Brush.linearGradient(listOf(F1CountdownStart, F1CountdownEnd)))
        ) {
            if (article.imageUrl != null) {
                // Decorative: the title written on top of the photo already describes the story.
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
            }
            // Dark fade so the white title stays readable on any photo.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = SCRIM_ALPHA)),
                            startY = 0f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Text(
                        text = stringResource(R.string.news_top_story),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = FEATURED_TITLE_LINES,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (article.summary.isNotEmpty()) {
                Text(
                    text = article.summary,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = FEATURED_SUMMARY_LINES,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.news_read_more),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

/** One compact headline: small photo on the left, title in the middle, red arrow on the right. */
@Composable
private fun NewsHeadlineRow(article: NewsArticle) {
    val uriHandler = LocalUriHandler.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { uriHandler.openUri(article.link) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (article.imageUrl != null) {
            // Decorative: the headline next to it already says what the story is about.
            AsyncImage(
                model = article.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(96.dp)
                    .aspectRatio(PHOTO_RATIO)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = article.title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = TITLE_LINES,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun NewsLoading() {
    Card(modifier = Modifier.fillMaxWidth(), border = F1CardBorder) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.news_loading),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/** News is a bonus: if it fails the rest of the home screen keeps working. */
@Composable
private fun NewsError(onRetry: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), border = F1CardBorder) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.news_error),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.action_retry))
            }
        }
    }
}

@Composable
private fun NewsMessage(text: String) {
    Card(modifier = Modifier.fillMaxWidth(), border = F1CardBorder) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsSectionPreview() {
    AppTheme {
        NewsSection(
            state = NewsUiState.Success(PreviewData.news),
            onRetry = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsSectionErrorPreview() {
    AppTheme {
        NewsSection(
            state = NewsUiState.Error,
            onRetry = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}