package com.gulftechiinovations.product_scanner.models

sealed class GetDataFromRemote<out T> {
    data object Loading: GetDataFromRemote<Nothing>()
    data class Success<T>(val data:T): GetDataFromRemote<T>()
    data class Failed(val error: Error): GetDataFromRemote<Nothing>()
}