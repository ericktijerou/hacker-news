package com.ericktijerou.hackernews.domain.di

import com.ericktijerou.hackernews.domain.interactor.NewsInteractor
import com.ericktijerou.hackernews.domain.interactor.implementation.NewsInteractorImp
import org.koin.dsl.module

internal val domainModules = module {
    single<NewsInteractor> { NewsInteractorImp(get()) }
}