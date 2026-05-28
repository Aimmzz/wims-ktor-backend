package com.wims.services

import com.wims.models.CreateProductRequest
import com.wims.models.Product
import com.wims.models.UpdateProductRequest
import com.wims.repositories.ProductRepository

class ProductServiceImpl(
    private val productRepository: ProductRepository
) : ProductService {
    override suspend fun getAllProducts(): ServiceResult<List<Product>> {
        val products = productRepository.findAll()
        return ServiceResult.Success(products)
    }

    override suspend fun getProductById(id: Int): ServiceResult<Product> {
        val product = productRepository.findById(id)
            ?: return ServiceResult.NotFound("Produk dengan ID $id tidak ditemukan")
        return ServiceResult.Success(product)
    }

    override suspend fun createProduct(request: CreateProductRequest): ServiceResult<Product> {
        val skuExists = productRepository.existsBySku(request.sku)
        if (skuExists) {
            return ServiceResult.Conflict("SKU '${request.sku}' sudah digunakan produk lain")
        }

        val product = Product(
            id = 0,
            name = request.name,
            sku = request.sku,
            price = request.price,
            stock = request.stock,
            minStock = request.minStock,
            categoryId = request.categoryId,
        )

        val savedProduct = productRepository.save(product)
        return ServiceResult.Success(savedProduct)
    }

    override suspend fun updateProduct(
        id: Int,
        request: UpdateProductRequest
    ): ServiceResult<Product> {
        productRepository.findById(id)
            ?: return ServiceResult.NotFound("Produk dengan ID $id tidak ditemukan")

        val skuConflict = productRepository.existsBySku(request.sku, excludeId = id)
        if (skuConflict) {
            return ServiceResult.Conflict("SKU '${request.sku}' sudah digunakan produk lain")
        }

        val updatedProduct = Product(
            id = id,
            name = request.name,
            sku = request.sku,
            price = request.price,
            stock = request.stock,
            minStock = request.minStock,
            categoryId = request.categoryId,
        )

        val result = productRepository.update(id, updatedProduct)
            ?: return ServiceResult.NotFound("Produk dengan ID $id tidak ditemukan")

        return ServiceResult.Success(result)
    }

    override suspend fun deleteProduct(id: Int): ServiceResult<Unit> {
        val deleted = productRepository.delete(id)
        if (!deleted) {
            return ServiceResult.NotFound("Produk dengan ID $id tidak ditemukan")
        }
        return ServiceResult.Success(Unit)
    }

    override suspend fun getLowStockProducts(): ServiceResult<List<Product>> {
        val products = productRepository.findAll()
        val lowStockProducts = products.filter { it.stock <= it.minStock}

        return ServiceResult.Success(lowStockProducts)
    }
}