package com.wims.repositories

import com.wims.models.Product

class ProductRepositoryImpl : ProductRepository {

    private val products = mutableListOf<Product>(
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

    override suspend fun findAll(): List<Product> = products.toList()

    override suspend fun findById(id: Int): Product? = products.find { it.id == id }

    override suspend fun save(product: Product): Product {
        val newProduct = product.copy(
            id = (products.maxOfOrNull { it.id } ?: 0) + 1
        )
        products.add(newProduct)
        return newProduct
    }

    override suspend fun update(id: Int, product: Product): Product? {
        val index = products.indexOfFirst { it.id == id }
        if (index == -1) return null

        val updatedProduct = product.copy(id = id)
        products[index] = updatedProduct
        return updatedProduct
    }

    override suspend fun delete(id: Int): Boolean = products.removeIf { it.id == id }

    override suspend fun existsBySku(sku: String, excludeId: Int?): Boolean =
        products.any { it.sku == sku && it.id != excludeId }
}