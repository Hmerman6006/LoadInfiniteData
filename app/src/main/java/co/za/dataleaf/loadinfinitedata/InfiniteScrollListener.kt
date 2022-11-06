package co.za.dataleaf.loadinfinitedata

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.za.dataleaf.loadinfinitedata.databinding.ActivityMainBinding

class InfiniteScrollListener(
    private val mLayoutManager: LinearLayoutManager,
    private val adapter: RecyclerViewAdapter
): RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5
    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    fun setLoaded() {
        isLoading = false
    }

    fun getLoaded(): Boolean {
        return isLoading
    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        Log.d("SCROLL_LISTENER ............... ", "onScrollStateChanged $newState")
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        totalItemCount = mLayoutManager.itemCount
        lastVisibleItem = mLayoutManager.findLastVisibleItemPosition()
        val item = adapter.getListItem(adapter.currentList.size)?.id ?: 0L
        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            mOnLoadMoreListener.onLoadMore(item + 1)
            isLoading = true
        }
    }
}