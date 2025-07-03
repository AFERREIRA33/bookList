package com.example.demo.domain


interface port {
    fun save(book: Book)
    fun findAll(): List<Book>
    fun reserve(title: String)
    fun findByTitle(title: String): Book?
}

