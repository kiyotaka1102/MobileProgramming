package com.example.dessertclicker.data

import androidx.annotation.DrawableRes
import com.example.dessertclicker.data.Datasource.dessertList

data class DessertUiState(
    val revenue: Int = 0,
    val dessertsSold1: Int = 0,
    val dessertsSold2: Int = 5,
    val currentDessertIndex1: Int = 0,
    val currentDessertIndex2: Int = 1,
    val currentDessertImageId1: Int = dessertList[0].imageId,
    val currentDessertImageId2: Int = dessertList[1].imageId,
    val currentDessertPrice1: Int = dessertList[0].price,
    val currentDessertPrice2: Int = dessertList[1].price
)
