package com.wims.routes

import com.wims.models.CreateProductRequest
import com.wims.models.UpdateProductRequest
import com.wims.models.failed
import com.wims.models.success
import com.wims.models.successMessage
import com.wims.services.ProductService
import com.wims.services.ServiceResult
import com.wims.validation.toFieldErrors
import com.wims.validation.validateCreateProduct
import com.wims.validation.validateUpdateProduct
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.productRoutes() {

    val productService: ProductService by inject()

    route("/products") {

        // GET /api/v1/products
        get {
            when (val result = productService.getAllProducts()) {
                is ServiceResult.Success -> call.respond(
                    HttpStatusCode.OK,
                    success(data = result.data)
                )

                else -> call.respond(HttpStatusCode.InternalServerError, failed("Gagal mengambil data produk"))
            }
        }

        get("/low-stock") {
            when (val result = productService.getLowStockProducts()) {
                is ServiceResult.Success -> call.respond(
                    HttpStatusCode.OK,
                    success(data = result.data)
                )
                else -> call.respond(
                    HttpStatusCode.InternalServerError,
                    failed("Gagal mengambil data produk low stock")
                )
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    failed("ID harus berupa angka")
                )

            when (val result = productService.getProductById(id)) {
                is ServiceResult.Success -> call.respond(
                    HttpStatusCode.OK,
                    success(data = result.data)
                )

                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    failed(result.message)
                )

                else -> call.respond(
                    HttpStatusCode.InternalServerError,
                    failed("Terjadi kesalahan")
                )
            }
        }

        post {
            val request = call.receive<CreateProductRequest>()

            val validationResult = validateCreateProduct(request)
            if (validationResult.errors.isNotEmpty()) {
                return@post call.respond(
                    HttpStatusCode.UnprocessableEntity,
                    failed("Data tidak valid", validationResult.toFieldErrors())
                )
            }

            when (val result = productService.createProduct(request)) {
                is ServiceResult.Success -> call.respond(
                    HttpStatusCode.Created,
                    success(data = result.data, message = "Produk berhasil ditambahkan")
                )

                is ServiceResult.Conflict -> call.respond(
                    HttpStatusCode.Conflict,
                    failed(result.message)
                )

                else -> call.respond(
                    HttpStatusCode.InternalServerError,
                    failed("Gagal menyimpan produk")
                )
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    failed("ID harus berupa angka")
                )

            val request = call.receive<UpdateProductRequest>()

            val validationResult = validateUpdateProduct(request)
            if (validationResult.errors.isNotEmpty()) {
                return@put call.respond(
                    HttpStatusCode.UnprocessableEntity,
                    failed("Data tidak valid", validationResult.toFieldErrors())
                )
            }

            when (val result = productService.updateProduct(id, request)) {
                is ServiceResult.Success -> call.respond(
                    HttpStatusCode.OK,
                    success(data = result.data, message = "Produk berhasil diupdate")
                )

                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    failed(result.message)
                )

                is ServiceResult.Conflict -> call.respond(
                    HttpStatusCode.Conflict,
                    failed(result.message)
                )

                else -> call.respond(
                    HttpStatusCode.InternalServerError,
                    failed("Gagal mengupdate produk")
                )
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    failed("ID harus berupa angka")
                )

            when (val result = productService.deleteProduct(id)) {
                is ServiceResult.Success -> call.respond(
                    HttpStatusCode.OK,
                    successMessage("Produk berhasil dihapus")
                )

                is ServiceResult.NotFound -> call.respond(
                    HttpStatusCode.NotFound,
                    failed(result.message)
                )

                else -> call.respond(
                    HttpStatusCode.InternalServerError,
                    failed("Gagal menghapus produk")
                )
            }
        }
    }
}