package com.wims.routes

import com.wims.models.CreateProductRequest
import com.wims.models.Product
import com.wims.models.UpdateProductRequest
import com.wims.models.failed
import com.wims.models.success
import com.wims.validation.toFieldErrors
import com.wims.validation.validateCreateProduct
import com.wims.validation.validateUpdateProduct
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val products = mutableListOf(
    Product(
        id = 1,
        name = "Laptop ASUS",
        sku = "LPT-001",
        price = 12000000.0,
        stock = 50,
        minStock = 5,
        categoryId = 1
    ),
    Product(
        id = 2,
        name = "Mouse Logitech",
        sku = "MSE-001",
        price = 350000.0,
        stock = 200,
        minStock = 20,
        categoryId = 2
    ),
)

fun Route.productRoutes() {
    route("/products") {

        // GET /api/v1/products
        get {
            call.respond(HttpStatusCode.OK, success(data = products))
        }

        // GET /api/v1/products/{id}
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, failed("ID harus berupa angka"))

            val product = products.find { it.id == id }
                ?: return@get call.respond(HttpStatusCode.NotFound, failed("Product dengan $id tidak ditemukan"))

            call.respond(HttpStatusCode.OK, success(data = product))
        }

        // POST /api/v1/products
        post {
            val request = call.receive<CreateProductRequest>()

            val validationResult = validateCreateProduct(request)
            if (validationResult.errors.isNotEmpty()) {
                return@post call.respond(
                    HttpStatusCode.UnprocessableEntity, failed(
                        message = "Data produk tidak valid",
                        errors = validationResult.toFieldErrors()
                    )
                )
            }

            val newProduct = Product(
                id = (products.maxOfOrNull { it.id } ?: 0) + 1,
                name = request.name,
                sku = request.sku,
                price = request.price,
                stock = request.stock,
                minStock = request.minStock,
                categoryId = request.categoryId
            )
            products.add(newProduct)

            call.respond(
                HttpStatusCode.Created,
                success(message = "Product berhasil ditambahkan", data = newProduct)
            )
        }

        // PUT /api/v1/products/{id}
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    failed("ID harus berupa angka")
                )

            val index = products.indexOfFirst { it.id == id }
            if (index == -1) {
                return@put call.respond(
                    HttpStatusCode.NotFound,
                    failed("Produk dengan ID $id tidak ditemukan")
                )
            }

            val request = call.receive<UpdateProductRequest>()

            val validationResult = validateUpdateProduct(request)
            if (validationResult.errors.isNotEmpty()) {
                return@put call.respond(
                    HttpStatusCode.UnprocessableEntity,
                    failed(
                        message = "Data produk tidak valid",
                        errors = validationResult.toFieldErrors()
                    )
                )
            }

            val updatedProduct = products[index].copy(
                name = request.name,
                sku = request.sku,
                price = request.price,
                stock = request.stock,
                minStock = request.minStock,
                categoryId = request.categoryId,
            )
            products[index] = updatedProduct

            call.respond(
                HttpStatusCode.OK,
                success(data = updatedProduct, message = "Produk berhasil diupdate")
            )
        }

        // DELETE /api/v1/products/{id}
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    failed("ID harus berupa angka")
                )

            val removed = products.removeIf { it.id == id }
            if (!removed) {
                return@delete call.respond(
                    HttpStatusCode.NotFound,
                    failed("Product dengan $id tidak ditemukan")
                )
            }

            call.respond(HttpStatusCode.OK, success<Nothing?>(data = null, message = "Product berhasil dihapus!"))
        }
    }
}