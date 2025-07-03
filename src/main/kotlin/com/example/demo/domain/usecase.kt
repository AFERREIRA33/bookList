package com.example.demo.domain

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service


@Service
class usecase(private val repository: port) {


    fun addBook(book: Book) {
        require(book.author.isNotEmpty()) { "Author should not be empty" }
        require(book.title.isNotEmpty()) { "Title should not be empty" }

        repository.save(book)
    }

    fun getAllBooks() : List<Book> {

        return repository.findAll().sortedBy { it.title }
    }
}