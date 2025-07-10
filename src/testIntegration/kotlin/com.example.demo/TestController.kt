package com.example.demo

import Infrastructure.application.driving.controller.BookController
import Infrastructure.application.driving.controller.DTO.BookDTO
import com.example.demo.domain.Book
import com.example.demo.domain.usecase
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.spec.style.Test
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.lang.IllegalArgumentException

@WebMvcTest(BookController::class)
class TestController(
    @Autowired val mockMvc: MockMvc,
    @MockkBean val bookService: usecase
) : FunSpec({

    test("POST /books should call addBook on service") {
        val json = """
            {
                "title": "Gamma",
                "author": "Author G"
            }
        """.trimIndent()

        every { bookService.addBook(Book("Gamma", "Author G")) } returns Unit

        val response = mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andReturn()

        response.response.status shouldBe 200

        verify(exactly = 1) {
            bookService.addBook(Book("Gamma", "Author G"))
        }
    }

    test("GET / should return list of books as JSON") {
        every { bookService.getAllBooks() } returns listOf(
            Book("Alpha", "Author A"),
            Book("Beta", "Author B")
        )

        val response = mockMvc.get("/books") {
            accept = MediaType.APPLICATION_JSON
        }.andReturn()


        val json = """[{"title":"Author A","author":"Alpha","reserved":false},{"title":"Author B","author":"Beta","reserved":false}]""".trimIndent()
        response.response.status shouldBe 200
        response.response.contentAsString shouldBe json
    }

    test("Invalid input: POST /books with empty JSON should return 400") {
        val result = mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content ="{}"
        }
            .andExpect {
                status { isBadRequest() }
            }
            .andReturn()
    }


    test("POST /books should return 500 when domain throws exception") {
        val json = """
        {
            "title": "ErrorBook",
            "author": "Author E",
            "reserved":false
        }
    """.trimIndent()

        every { bookService.addBook(Book("ErrorBook", "Author E")) } throws RuntimeException("Erreur interne")

        val response = mockMvc.post("/books") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andReturn()

        response.response.status shouldBe 500

    }

    // Test integration reserved book
    test("POST /books/reserve should return 200 when successful") {
        every { bookService.reserveBook("1984") } returns Unit

        val json = """{ "title": "1984", "author": "Orwell" }"""

        val response = mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andReturn()

        response.response.status shouldBe 200
    }

    test("POST /books/reserve should return 409 when already reserved") {
        every { bookService.reserveBook("1984") } throws IllegalArgumentException("Déjà réservé")

        val json = """{ "title": "1984", "author": "Orwell" }"""

        val response = mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andReturn()

        response.response.status shouldBe 409
        response.response.contentAsString shouldBe "Déjà réservé"
    }

    test("POST /books/reserve should return 500 on unknown error") {
        every { bookService.reserveBook("1984") } throws RuntimeException("Erreur")

        val json = """{ "title": "1984", "author": "Orwell" }"""

        val response = mockMvc.post("/books/reserve") {
            contentType = MediaType.APPLICATION_JSON
            content = json
        }.andReturn()

        response.response.status shouldBe 500
    }
})