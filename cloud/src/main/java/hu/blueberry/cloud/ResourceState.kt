package hu.blueberry.cloud

import java.nio.ReadOnlyBufferException

sealed class ResourceState<T> {

    class Loading<T> : ResourceState<T>()

    class Success<T> (val data:T) : ResourceState<T>()

    class Error<T> (val error:Any) : ResourceState<T>()
}


sealed class GoogleResponse<K> {

    abstract val successful :Boolean

    abstract val data: K?

    abstract val exception: Exception?
    class Successful<K>(override val data: K) : GoogleResponse<K>(){

        override val exception:Exception?
        get() = null

        override val successful: Boolean
            get() = true
    }

    class Error<K>(override val exception: Exception) : GoogleResponse<K>(){
        override val successful: Boolean
            get() = false

        override val data:K?
            get() = null
    }
}