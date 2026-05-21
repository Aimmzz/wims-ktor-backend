package com.wims.validation

import com.wims.models.CreateProductRequest
import com.wims.models.FieldError
import com.wims.models.UpdateProductRequest
import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.minimum
import io.konform.validation.constraints.pattern

val validateCreateProduct = Validation<CreateProductRequest> {
    CreateProductRequest::name {
        minLength(3) hint "Nama produk minimal 3 karakter"
        maxLength(100) hint "Nama produk maksimal 100 karakter"
    }
    CreateProductRequest::sku {
        minLength(3) hint "SKU minimal 3 karakter"
        maxLength(20) hint "SKU maksimal 20 karakter"
        // Format SKU: huruf kapital + angka + strip, contoh: LPT-001
        pattern(Regex("^[A-Z0-9\\-]+$")) hint "SKU hanya boleh huruf kapital, angka, dan tanda strip"
    }
    CreateProductRequest::price {
        minimum(0.0) hint "Harga tidak boleh negatif"
    }
    CreateProductRequest::stock {
        minimum(0) hint "Stok tidak boleh negatif"
    }
    CreateProductRequest::minStock {
        minimum(0) hint "Minimum stok tidak boleh negatif"
    }
    CreateProductRequest::categoryId {
        minimum(1) hint "Category ID tidak valid"
    }
    constrain("Stok minimum tidak boleh lebih besar dari stok awal") {
        it.minStock <= it.stock
    }
}

val validateUpdateProduct = Validation<UpdateProductRequest> {
    UpdateProductRequest::name {
        minLength(3) hint "Nama produk minimal 3 karakter"
        maxLength(100) hint "Nama produk maksimal 100 karakter"
    }
    UpdateProductRequest::sku {
        minLength(3) hint "SKU minimal 3 karakter"
        maxLength(20) hint "SKU maksimal 20 karakter"
        pattern(Regex("^[A-Z0-9\\-]+$")) hint "SKU hanya boleh huruf kapital, angka, dan tanda strip"
    }
    UpdateProductRequest::price {
        minimum(0.0) hint "Harga tidak boleh negatif"
    }
    UpdateProductRequest::stock {
        minimum(0) hint "Stok tidak boleh negatif"
    }
    UpdateProductRequest::minStock {
        minimum(0) hint "Minimum stok tidak boleh negatif"
    }
    constrain("Stok minimum tidak boleh lebih besar dari stok awal") {
        it.minStock <= it.stock
    }
}

// ── Helper: Konform Result → FieldError List ──────────────────────────────────

fun <T> ValidationResult<T>.toFieldErrors(): List<FieldError> =
    errors.map { error ->
        FieldError(
            // Konform memberi path seperti ".name" atau ".sku" — kita hapus titik di depan
            field = error.dataPath.trimStart('.').ifEmpty { "minStock" },
            message = error.message
        )
    }