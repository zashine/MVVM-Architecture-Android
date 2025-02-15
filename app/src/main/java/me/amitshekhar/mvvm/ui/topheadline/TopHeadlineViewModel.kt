package me.amitshekhar.mvvm.ui.topheadline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import me.amitshekhar.mvvm.data.model.Article
import me.amitshekhar.mvvm.data.repository.TopHeadlineRepository
import me.amitshekhar.mvvm.ui.base.UiState
import kotlin.random.Random

class TopHeadlineViewModel(private val topHeadlineRepository: TopHeadlineRepository) : ViewModel() {

    /**
     * 个人理解：
     * viewModel用法，声明一个可变的private对象并使其不对外暴露 (即外部无法直接调用并update)；
     * 同时，声明一个不可变的public对象与私有对象绑定 (下面的使用中：MutableStateFlow是StateFlow的子类，面相对象的多态性)
     */

    private val _uiState = MutableStateFlow<UiState<List<Article>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Article>>> = _uiState

    init {
        fetchTopHeadlines()
    }

    private fun fetchTopHeadlines() {
        viewModelScope.launch {
            //topHeadlineRepository.getTopHeadlines(COUNTRY)
            topHeadlineRepository.getTopHeadlinesWithoutParam()
                .catch { e ->
                    _uiState.value = UiState.Error(e.toString())
                }
                .collect {
                    _uiState.value = UiState.Success(it)
                }
        }
    }

    private val _privateData = MutableLiveData<String>("")
    val publicData: LiveData<String> = _privateData

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            // 模拟获取数据的过程
            val randomIntString = Random.nextInt(100).toString()
            // 更新
            _privateData.value = randomIntString
        }
    }
}