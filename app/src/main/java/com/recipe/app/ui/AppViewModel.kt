package com.recipe.app.ui

import androidx.lifecycle.ViewModel
import com.recipe.app.utils.Injector
import kotlinx.coroutines.*

open class AppViewModel: ViewModel() {

    val dispatcherProvider = Injector.getCoroutinesDispatcherProvider()

    private val job = Job()
    val scope = CoroutineScope(Dispatchers.Main + job)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }

    fun runOnMainThread(action: () -> Unit) {
        scope.launch {
            withContext(dispatcherProvider.main) {
                action.invoke()
            }
        }
    }

}