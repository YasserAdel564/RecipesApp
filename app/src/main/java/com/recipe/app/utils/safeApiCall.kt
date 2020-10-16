package com.recipe.app.utils

import java.io.IOException


suspend fun <T : Any> safeApiCall(call: suspend () -> DataResource<T>, errorMessage: String): DataResource<T> {
    return try {
        call()
    } catch (e: Exception) {
        e.printStackTrace()
        DataResource.Error(IOException(errorMessage, e))
    }
}