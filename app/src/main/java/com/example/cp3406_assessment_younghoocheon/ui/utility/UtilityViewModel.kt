package com.example.cp3406_assessment_younghoocheon.ui.utility

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cp3406_assessment_younghoocheon.UtilityApplication
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceError
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceLoadException
import com.example.cp3406_assessment_younghoocheon.data.repository.RaceRepository
import com.example.cp3406_assessment_younghoocheon.domain.UtilitySettings
import java.time.Instant
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

private const val TICK_INTERVAL_MS = 1_000L
private const val STOP_TIMEOUT_MS = 5_000L

/**
 * Holds the state shared by the Race screen and the Settings screen.
 * Both screens use the same instance, so a settings change immediately updates the main screen.
 */
class UtilityViewModel(
    private val repository: RaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UtilityUiState>(UtilityUiState.Loading)

    /** Loading / success / error state of the next-race request. */
    val uiState: StateFlow<UtilityUiState> = _uiState.asStateFlow()

    private val _settings = MutableStateFlow(UtilitySettings())

    /** Current user preferences (in memory only). */
    val settings: StateFlow<UtilitySettings> = _settings.asStateFlow()

    /**
     * Current time, updated every second while the UI is visible.
     * `WhileSubscribed` stops the ticking when the app goes to the background.
     */
    val now: StateFlow<Instant> = flow {
        while (true) {
            emit(Instant.now())
            delay(TICK_INTERVAL_MS)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = Instant.now()
    )

    private var loadJob: Job? = null

    init {
        refresh()
    }

    /** (Re)loads the next race. A request that is still running is cancelled first. */
    fun refresh() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.value = UtilityUiState.Loading
            _uiState.value = repository.getNextRace().fold(
                onSuccess = { race -> UtilityUiState.Success(race) },
                onFailure = { throwable ->
                    val error = (throwable as? RaceLoadException)?.error ?: RaceError.DATA
                    UtilityUiState.Error(error)
                }
            )
        }
    }

    /** Replaces the current preferences. */
    fun updateSettings(newSettings: UtilitySettings) {
        _settings.value = newSettings
    }

    companion object {
        /** Creates the ViewModel with the repository from the application's container. */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as UtilityApplication
                UtilityViewModel(application.container.raceRepository)
            }
        }
    }
}
