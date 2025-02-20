package me.amitshekhar.mvvm.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.amitshekhar.mvvm.data.api.NetworkService
import me.amitshekhar.mvvm.data.model.Article
import me.amitshekhar.mvvm.data.model.TopHeadlinesResponse
import me.amitshekhar.mvvm.ui.flow.new.DataStatus
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopHeadlineRepository @Inject constructor(private val networkService: NetworkService) {

    fun getTopHeadlines(country: String): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadlines(country))
        }.map {
            it.articles
        }
    }

    fun getTopHeadlinesWithoutParam(): Flow<List<Article>> {
        return flow {
            emit(networkService.getTopHeadlines2())
        }.map {
            it.articles
        }
    }

    suspend fun getCoinsList() = flow {
        emit(DataStatus.loading())
        val result = networkService.getTopHeadlines3()
        when (result.code()) {
            200 -> {
                emit(DataStatus.success(result.body()))
            }
            else -> {
                emit(DataStatus.error(result.message()))
            }
        }
    }.catch {
        emit(DataStatus.error(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}