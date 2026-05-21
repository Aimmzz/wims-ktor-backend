package com.wims.plugins

import com.wims.models.failed
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.SerializationException

fun Application.configureStatusPages() {
    install(StatusPages) {

        // ── Serialization Error ───────────────────────────────────────────────
        exception<SerializationException> { call, cause ->
            call.application.log.warn("Serialization error: ${cause.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                failed("Format JSON tidak valid: ${cause.message}")
            )
        }

        // ── Illegal Argument ─────────────────────────────────────────────────
        exception<IllegalArgumentException> { call, cause ->
            call.application.log.warn("Illegal argument: ${cause.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                failed(cause.message ?: "Request tidak valid")
            )
        }

        // ── Catch-All: Semua Exception Lain ──────────────────────────────────
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception", cause)
            call.respond(
                HttpStatusCode.InternalServerError,
                failed("Terjadi kesalahan internal pada server")
            )
        }

        // ── HTTP Status Handler ───────────────────────────────────────────────
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(
                status,
                failed("Endpoint '${call.request.local.uri}' tidak ditemukan")
            )
        }

        // Tangkap method yang tidak diizinkan
        status(HttpStatusCode.MethodNotAllowed) { call, status ->
            call.respond(
                status,
                failed("Method tidak diizinkan untuk endpoint ini")
            )
        }
    }
}