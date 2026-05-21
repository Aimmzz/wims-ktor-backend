package com.wims.models

import kotlinx.serialization.Serializable

@Serializable
data class Product (
    val id: Int,
    val name: String,
    val sku: String,
    val price: Double,
    val stock: Int,
    val minStock: Int,
    val categoryId: Int,
)

@Serializable
data class CreateProductRequest (
    val id: Int,
    val name: String,
    val sku: String,
    val price: Double,
    val stock: Int,
    val minStock: Int,
    val categoryId: Int,
)

@Serializable
data class UpdateProductRequest (
    val id: Int,
    val name: String,
    val sku: String,
    val price: Double,
    val stock: Int,
    val minStock: Int,
    val categoryId: Int,
)