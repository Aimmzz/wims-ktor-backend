package com.wims.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class HealthResponse(
    val status: String,
    val service: String,
    val version: String
)

fun Route.healthRoutes() {
    get("/health") {
        call.respond(
            status = HttpStatusCode.OK,
            message = HealthResponse(
                status = "UP",
                service = "WIMS API",
                version = "1.0.0"
            )
        )
    }
}