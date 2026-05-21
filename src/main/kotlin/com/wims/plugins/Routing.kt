package com.wims.plugins

import com.wims.routes.healthRoutes
import com.wims.routes.productRoutes
import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        healthRoutes()

        route("api/v1") {
            productRoutes()
        }
    }
}