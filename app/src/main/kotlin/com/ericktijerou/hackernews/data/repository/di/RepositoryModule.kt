package com.ericktijerou.hackernews.data.repository.di

import com.ericktijerou.hackernews.data.repository.NewsRepositoryImp
import com.ericktijerou.hackernews.domain.repository.NewsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<NewsRepository> { NewsRepositoryImp(get(), get()) }
}