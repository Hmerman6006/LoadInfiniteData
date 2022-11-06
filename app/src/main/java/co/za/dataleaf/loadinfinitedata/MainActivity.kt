package co.za.dataleaf.loadinfinitedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.za.dataleaf.loadinfinitedata.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel = MainViewModel()
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecyclerViewAdapter
    lateinit var infiniteScrollListener: InfiniteScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RecyclerViewAdapter {
            grabItem(it)
        }

        val dividerItemDecoration = DividerItemDecoration(this, RecyclerView.VERTICAL)
        binding.rvInfinite.addItemDecoration(dividerItemDecoration)

        mLayoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.rvInfinite.layoutManager = mLayoutManager
        binding.rvInfinite.setHasFixedSize(false)
        binding.rvInfinite.adapter = adapter
        infiniteScrollListener = InfiniteScrollListener(mLayoutManager, adapter)
        infiniteScrollListener.setOnLoadMoreListener(object: OnLoadMoreListener {
            override fun onLoadMore(start: Long) {
                viewModel.loadMoreData(start)
            }
        })
        binding.rvInfinite.addOnScrollListener(infiniteScrollListener)

        subscribeOnObserver()
        viewModel.loadMoreData()

        binding.btnTop.setOnClickListener {
            binding.rvInfinite.scrollToPosition(0)
        }
    }

    private fun subscribeOnObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadChannel.collectLatest {
                    when(it.apiStatus) {
                        ApiStatus.LOADING -> {
                            Log.d("MainActivity loadChannel", "LOADING ... ")
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        ApiStatus.SUCCESS -> {
                            Log.d("MainActivity loadChannel", "SUCCESS  ... ${it.data?.size} ${adapter.currentList.size} ${adapter.getListItem(adapter.currentList.size)}")
                            binding.progressBar.visibility = View.GONE
                            if (it.data != null) {
                                // Add data to existing list
                                // is only needed if all loaded data should subsist. Better solution would be to keep
                                // ListAdapter items at a fixed length and add negative scrolling to subtract items from list.
                                adapter.setData(it.data)
                                adapter.submitList(adapter.showData())
                                binding.tvPageProgress.setText(mLayoutManager.itemCount.toString())
                            }
                            infiniteScrollListener.setLoaded()
                        }
                        ApiStatus.ERROR -> {
                            Log.d("MainActivity loadChannel", "ERROR ... ")
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun grabItem(item: InfiniteHolder) {
        Log.d("MainActivity", item.toString())
    }
}