package com.example.cp3406_assessment_younghoocheon.data.repository

import com.example.cp3406_assessment_younghoocheon.data.model.NewsArticle

/** Source of the latest Formula 1 headlines. */
interface NewsRepository {

    /** Loads the newest headlines (newest first); any failure is returned as a failed Result. */
    suspend fun getLatestNews(): Result<List<NewsArticle>>
}