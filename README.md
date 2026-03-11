# 🏭 WIMS - Warehouse Inventory Management System
**Ktor REST API Bootcamp: Zero to Hero**

Repositori ini berisi perjalanan belajar saya dalam membangun sistem manajemen inventaris gudang menggunakan **Ktor Server**. Fokus utama proyek ini adalah mengimplementasikan arsitektur backend yang modern, *non-blocking*, dan *testable*.

## 📖 Deskripsi Sistem
WIMS adalah REST API untuk mengelola stok gudang, mencakup manajemen produk berdasarkan kategori, pencatatan stok masuk/keluar (Stock Moves), dan autentikasi pengguna dengan Role-Based Access Control (RBAC).

## 🛠 Tech Stack
| Komponen | Library |
| :--- | :--- |
| **Server Framework** | Ktor + Netty |
| **Serialization** | kotlinx.serialization |
| **Validation** | Konform |
| **Dependency Injection** | Koin |
| **ORM / Database** | Exposed DSL + PostgreSQL |
| **Migration & Pool** | Flyway + HikariCP |
| **Security** | JWT (nimbus-jose) |
| **Testing** | JUnit 5 + MockK + Ktor Test |

## 🗂️ Domain & Entity
Sistem ini mengelola entitas utama berikut:
- **Users**: Pengelola sistem (Admin/Staff).
- **Categories & Products**: Katalog barang dengan SKU unik.
- **Suppliers**: Penyedia barang.
- **Stock Moves**: Log transaksi stok (IN untuk barang masuk, OUT untuk barang keluar).

---

## 📋 Kurikulum & Progres
Saya mentracking progres belajar melalui *branching system* per modul:

### 🔹 Modul 1: Project Setup & Routing Dasar (`feat/module-1`)
* [ ] Setup Gradle & Ktor Engine.
* [ ] Struktur folder: `domain`, `plugins`, `routes`.
* [ ] Endpoint Health Check & Produk (In-memory).
* [ ] **Konsep:** Coroutines & Non-blocking design.

### 🔹 Modul 2: Serialization & Validation (`feat/module-2`)
* [ ] JSON Handling untuk Product & Stock.
* [ ] Validasi SKU, format email, dan quantity menggunakan **Konform**.
* [ ] Global Error Handling (StatusPages).

### 🔹 Modul 3: Dependency Injection & Layered Architecture (`feat/module-3`)
* [ ] Setup Koin.
* [ ] Refactor: `Route` -> `Service` -> `Repository`.

### 🔹 Modul 4: Database & Persistence (`feat/module-4`)
* [ ] Integrasi PostgreSQL & Exposed.
* [ ] Implementasi transaksi stok (Atomicity).
* [ ] Database Migration dengan Flyway.

### 🔹 Modul 5: Security & JWT (`feat/module-5`)
* [ ] Auth System (Register/Login).
* [ ] Protected Routes untuk manajemen stok.

### 🔹 Modul 6: Testing (`feat/module-6`)
* [ ] Unit Testing & Integration Testing.

### 🔹 Modul 7: Deployment (`feat/module-7`)
* [ ] Dockerization & Production Config.

---
*Created with ❤️ by Rohim Kurniawan*