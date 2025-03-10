package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.data.Datasource.dessertList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {

    private val _dessertUiState = MutableStateFlow(DessertUiState())
    val dessertUiState: StateFlow<DessertUiState> = _dessertUiState.asStateFlow()

    fun onDessertClicked(dessertNumber: Int) {
        _dessertUiState.update { state ->
            val newState = if (dessertNumber == 1) {
                val dessertsSold1 = state.dessertsSold1 + 1
                val nextDessertIndex1 = determineDessertIndex(dessertsSold1)

                state.copy(
                    revenue = state.revenue + state.currentDessertPrice1,
                    dessertsSold1 = dessertsSold1, // Chỉ cập nhật dessertsSold1
                    currentDessertIndex1 = nextDessertIndex1,
                    currentDessertImageId1 = dessertList[nextDessertIndex1].imageId,
                    currentDessertPrice1 = dessertList[nextDessertIndex1].price
                )
            } else {
                val dessertsSold2 = state.dessertsSold2 + 1
                val nextDessertIndex2 = determineDessertIndex(dessertsSold2)

                state.copy(
                    revenue = state.revenue + state.currentDessertPrice2,
                    dessertsSold2 = dessertsSold2, // Chỉ cập nhật dessertsSold2
                    currentDessertIndex2 = nextDessertIndex2,
                    currentDessertImageId2 = dessertList[nextDessertIndex2].imageId,
                    currentDessertPrice2 = dessertList[nextDessertIndex2].price
                )
            }
            newState
        }
    }

    private fun determineDessertIndex(dessertsSold: Int): Int {
        var dessertIndex = 0
        for (index in dessertList.indices) {
            if (dessertsSold >= dessertList[index].startProductionAmount) {
                dessertIndex = index
            } else {
                break
            }
        }
        return dessertIndex
    }
}
