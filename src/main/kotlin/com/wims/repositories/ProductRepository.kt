package com.wims.repositories

import com.wims.models.Product

interface ProductRepository {
    suspend fun findAll(): List<Product>
    suspend fun findById(id: Int): Product?
    suspend fun save(product: Product): Product
    suspend fun update(id: Int, product: Product): Product?
    suspend fun delete(id: Int): Boolean
    suspend fun existsBySku(sku: String, excludeId: Int? = null): Boolean
}