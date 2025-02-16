package me.amitshekhar.mvvm.di.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import me.amitshekhar.mvvm.ui.topheadline.TopHeadlineAdapter

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    // 不需要在手动委托 viewmodel ，因为使用 by viewModels() 委托来获取 ViewModel 实例

    @Provides
    fun provideTopHeadlineAdapter() = TopHeadlineAdapter(ArrayList())

}