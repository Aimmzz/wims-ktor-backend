package com.wims.services

import com.wims.models.CreateProductRequest
import com.wims.models.Product
import com.wims.models.UpdateProductRequest

interface ProductService {
    suspend fun getAllProducts(): ServiceResult<List<Product>>
    suspend fun getProductById(id: Int): ServiceResult<Product>
    suspend fun createProduct(request: CreateProductRequest): ServiceResult<Product>
    suspend fun updateProduct(id: Int, request: UpdateProductRequest): ServiceResult<Product>
    suspend fun deleteProduct(id: Int): ServiceResult<Unit>
    suspend fun getLowStockProducts(): ServiceResult<List<Product>>
}