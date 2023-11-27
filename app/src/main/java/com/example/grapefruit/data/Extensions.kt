package com.example.grapefruit.data

import hu.blueberry.cloud.ResourceState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

suspend fun <T> handleWithFlow (call: suspend () -> T) : Flow<ResourceState<T>> {
    return flow{
        emit(ResourceState.Loading())

        val response = call()

        emit(ResourceState.Success(response))

    }.catch{
            e -> emit(ResourceState.Error(e))
    }.flowOn(Dispatchers.IO)
}