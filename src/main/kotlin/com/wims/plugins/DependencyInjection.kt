package com.wims.plugins

import com.wims.repositories.ProductRepository
import com.wims.repositories.ProductRepositoryImpl
import com.wims.services.ProductService
import com.wims.services.ProductServiceImpl
import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

val appModule = module {

    single<ProductRepository> { ProductRepositoryImpl() }

    single<ProductService> { ProductServiceImpl(get()) }
}

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }
}