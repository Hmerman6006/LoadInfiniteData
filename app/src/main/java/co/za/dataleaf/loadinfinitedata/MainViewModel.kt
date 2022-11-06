package co.za.dataleaf.loadinfinitedata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private lateinit var loadMoreItems: ArrayList<InfiniteHolder>

    private val _loadChannel = Channel<NetworkResource<ArrayList<InfiniteHolder>>>()
    val loadChannel = _loadChannel.receiveAsFlow()

    fun loadMoreData(start: Long = 0) {
        viewModelScope.launch {
            _loadChannel.send(NetworkResource.loading(null))
            try {
                delay(2000L)
                loadMoreItems = ArrayList()
                val end = start + 16
                for(i in start..end) {
                    loadMoreItems.add(
                        InfiniteHolder(
                            i, "https://dataleaf.co.za", "Item $i"
                        )
                    )
                }
                _loadChannel.send(NetworkResource.success(loadMoreItems))
            } catch (e: Exception) {
                _loadChannel.send(NetworkResource.error(null, e.message  ?: "Error: $e"))
            }
        }

    }
}