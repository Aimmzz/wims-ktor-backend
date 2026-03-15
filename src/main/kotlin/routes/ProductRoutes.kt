package routes

import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val name: String,
    val sku: String,
    val stock: Int,
)

@Serializable
data class ApiResponse<T>(
    val message: String? = null,
    val data: T? = null,
    val error: String? = null,
)

private val products = mutableListOf(
    Product(id = 1, name = "Laptop ASUS", sku = "LPT-001", stock = 50),
    Product(id = 2, name = "Mouse Logitech", sku = "MSE-001", stock = 200),
)

fun Route.productRoutes() {
    route("/products") {

        // GET /api/v1/products
        get {
            call.respond(HttpStatusCode.OK, ApiResponse(data = products))
        }

        // GET /api/v1/products/{id}
        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@get call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(error = "ID harus berupa angka"))

            val product = products.find { it.id == id }
                ?: return@get call.respond(HttpStatusCode.NotFound, ApiResponse<Product>(error = "Product dengan $id tidak ditemukan"))

            call.respond(HttpStatusCode.OK, ApiResponse(data = product))
        }

        // POST /api/v1/products
        post {
            val newProduct = call.receive<Product>().copy(id = products.size + 1)
            products.add(newProduct)

            call.respond(HttpStatusCode.Created, ApiResponse(message = "Product berhasil ditambahkan", data = newProduct))
        }

        // DELETE /api/v1/products/{id}
        delete("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@delete call.respond(HttpStatusCode.BadRequest, ApiResponse<Product>(error = "ID harus berupa angka"))

            val removed = products.removeIf { it.id == id }
            if (!removed) {
                return@delete call.respond(HttpStatusCode.NotFound, ApiResponse<Product>(error = "Product dengan $id tidak ditemukan"))
            }

            call.respond(HttpStatusCode.OK, ApiResponse<Product>(message = "Product berhasil dihapus!"))
        }

        // PUT /api/v1/products/{id}
        put("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
                ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Product>(error = "ID harus berupa angka")
                )

            val index = products.indexOfFirst { it.id == id }
            if (index == -1) {
                return@put call.respond(
                    HttpStatusCode.NotFound,
                    ApiResponse<Product>(error = "Produk dengan ID $id tidak ditemukan")
                )
            }

            val updatedProduct = call.receive<Product>().copy(id = id)
            products[index] = updatedProduct

            call.respond(
                HttpStatusCode.OK,
                ApiResponse(message = "Produk berhasil diupdate", data = updatedProduct)
            )
        }
    }
}