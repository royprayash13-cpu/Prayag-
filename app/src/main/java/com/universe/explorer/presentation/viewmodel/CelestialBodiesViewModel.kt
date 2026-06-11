package com.universe.explorer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universe.explorer.data.models.CelestialBody
import com.universe.explorer.domain.usecase.GetCelestialBodiesUseCase
import com.universe.explorer.domain.usecase.SearchCelestialBodiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

data class CelestialBodiesUiState(
    val celestialBodies: List<CelestialBody> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = ""
)

@HiltViewModel
class CelestialBodiesViewModel @Inject constructor(
    private val getCelestialBodiesUseCase: GetCelestialBodiesUseCase,
    private val searchCelestialBodiesUseCase: SearchCelestialBodiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CelestialBodiesUiState())
    val uiState: StateFlow<CelestialBodiesUiState> = _uiState.asStateFlow()

    init {
        loadCelestialBodies()
    }

    fun loadCelestialBodies() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        getCelestialBodiesUseCase()
            .onEach { bodies ->
                _uiState.value = _uiState.value.copy(
                    celestialBodies = bodies,
                    isLoading = false,
                    error = null
                )
            }
            .catch { error ->
                Timber.e(error, "Error loading celestial bodies")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message ?: "Unknown error"
                )
            }
            .launchIn(viewModelScope)
    }

    fun search(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query, isLoading = true)

        if (query.isEmpty()) {
            loadCelestialBodies()
            return
        }

        searchCelestialBodiesUseCase(query)
            .onEach { bodies ->
                _uiState.value = _uiState.value.copy(
                    celestialBodies = bodies,
                    isLoading = false
                )
            }
            .catch { error ->
                Timber.e(error, "Error searching celestial bodies")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = error.message
                )
            }
            .launchIn(viewModelScope)
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(searchQuery = "")
        loadCelestialBodies()
    }
}
