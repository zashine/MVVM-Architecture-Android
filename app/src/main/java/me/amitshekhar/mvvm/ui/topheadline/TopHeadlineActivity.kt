package me.amitshekhar.mvvm.ui.topheadline

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.amitshekhar.mvvm.data.model.Article
import me.amitshekhar.mvvm.databinding.ActivityTopHeadlineBinding
import me.amitshekhar.mvvm.ui.base.UiState
import javax.inject.Inject

@AndroidEntryPoint
class TopHeadlineActivity : AppCompatActivity() {

    // 使用 by viewModels() 委托
    private val topHeadlineViewModel: TopHeadlineViewModel by viewModels()

    @Inject
    lateinit var adapter: TopHeadlineAdapter

    private lateinit var binding: ActivityTopHeadlineBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopHeadlineBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                topHeadlineViewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            renderList(it.data)
                            binding.recyclerView.visibility = View.VISIBLE
                        }

                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            //Handle Error
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@TopHeadlineActivity, it.message, Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
            }
        }

        topHeadlineViewModel.publicData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun renderList(articleList: List<Article>) {
        adapter.addData(articleList)
        adapter.notifyDataSetChanged()
    }

    /**
     * 这段代码使用 Dagger 依赖注入框架，它的作用是：
     *
     * 创建 DaggerActivityComponent 实例：
     *
     * DaggerActivityComponent.builder()：创建一个 DaggerActivityComponent 的构建器。
     * .applicationComponent((application as MVVMApplication).applicationComponent)：将应用程序级别的组件（applicationComponent）作为依赖提供给 DaggerActivityComponent。这通常用于提供应用程序范围的单例实例，例如网络请求库、数据库等。
     * .activityModule(ActivityModule(this))：使用 ActivityModule 为 DaggerActivityComponent 提供 Activity 级别的依赖。ActivityModule 负责创建 Activity 相关的实例，例如 Presenter、ViewModel 等。
     * .build()：构建 DaggerActivityComponent 实例。
     * 执行注入：
     *
     * .inject(this)：将 DaggerActivityComponent 的实例注入到当前的 Activity 中。这意味着 Dagger 会查找 Activity 中使用 @Inject 注解标记的成员变量，并使用 DaggerActivityComponent 中提供的依赖来初始化它们。
     * 总结：
     * 这段代码的目的是使用 Dagger 框架，通过依赖注入的方式来初始化 Activity 中所需的成员变量。DaggerActivityComponent 作为一个桥梁，连接了应用程序级别的依赖、Activity 级别的依赖和需要注入的 Activity。
     */
    /*private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as MVVMApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }*/

}
