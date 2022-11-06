package co.za.dataleaf.loadinfinitedata

data class NetworkResource<out T>(val apiStatus: ApiStatus, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): NetworkResource<T> = NetworkResource(apiStatus = ApiStatus.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): NetworkResource<T> =
            NetworkResource(apiStatus = ApiStatus.ERROR, data = data, message = message)

        fun <T> loading(data: T?): NetworkResource<T> = NetworkResource(apiStatus = ApiStatus.LOADING, data = data, message = null)
    }
}