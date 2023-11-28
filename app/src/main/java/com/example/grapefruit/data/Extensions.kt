package com.example.grapefruit.data

import android.util.Log
import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

suspend fun <T> handleWithFlow(call: suspend () -> T): Flow<ResourceState<T>> {
    return flow {
        emit(ResourceState.Loading())

        val response = call()

        emit(ResourceState.Success(response))

    }.catch { e ->
        emit(ResourceState.Error(e))
    }.flowOn(Dispatchers.IO)
}

fun <T> handleResponse(
    resource:ResourceState<T>,
    onInitial: () -> Unit = {},
    onLoading: () -> Unit = {},
    onSuccess: (data: T) -> Unit = {},
    onError: (error: Any) -> Unit = {},
) {
    when(resource){
        is ResourceState.Initial -> onInitial()
        is ResourceState.Loading -> onLoading()
        is  ResourceState.Success -> onSuccess(resource.data)
        is ResourceState.Error -> onError(resource.error)
    }
}
