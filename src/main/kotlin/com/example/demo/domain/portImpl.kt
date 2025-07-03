package com.example.demo.domain

import org.springframework.stereotype.Component


@Component
class portImpl : port {
    override fun save(book: Book) {
        TODO("Not yet implemented")
    }

    override fun findAll(): List<Book> {
        TODO("Not yet implemented")
    }

    override fun reserve(title: String) {
        TODO("Not yet implemented")
    }

    override fun findByTitle(title: String): Book? {
        TODO("Not yet implemented")
    }
}