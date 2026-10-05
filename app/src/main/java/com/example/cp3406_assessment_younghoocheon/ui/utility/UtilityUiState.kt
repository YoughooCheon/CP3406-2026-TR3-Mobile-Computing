package com.example.cp3406_assessment_younghoocheon.ui.utility

import com.example.cp3406_assessment_younghoocheon.data.model.Race
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceError

/** Everything the main screen can be showing. */
sealed interface UtilityUiState {

    data object Loading : UtilityUiState

    data class Success(val race: Race) : UtilityUiState

    data class Error(val error: RaceError) : UtilityUiState
}
