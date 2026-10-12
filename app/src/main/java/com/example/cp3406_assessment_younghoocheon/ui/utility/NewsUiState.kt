package com.example.cp3406_assessment_younghoocheon.ui.utility

import com.example.cp3406_assessment_younghoocheon.data.model.NewsArticle

/** Everything the news section can be showing. */
sealed interface NewsUiState {

    data object Loading : NewsUiState

    data class Success(val articles: List<NewsArticle>) : NewsUiState

    data object Error : NewsUiState
}