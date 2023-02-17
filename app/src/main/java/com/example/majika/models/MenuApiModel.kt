package com.example.majika.models

data class MenuApiModel(
    val data: List<MenuModel?>?,
    val size: Int?
) {
    val isSuccessful: Boolean
        get() {
            return data != null
        }
}