package plugins

import io.ktor.server.application.Application
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import routes.healthRoutes
import routes.productRoutes

fun Application.configureRouting() {
    routing {
        healthRoutes()

        route("api/v1") {
            productRoutes()
        }
    }
}