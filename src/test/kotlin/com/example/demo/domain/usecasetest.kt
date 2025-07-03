package com.example.demo.domain
import com.example.demo.domain.Book
import com.example.demo.domain.port
import com.example.demo.domain.usecase
import io.kotest.assertions.throwables.shouldThrowWithMessage
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.lang.IllegalArgumentException

class usecasetest : FunSpec({
    val repository = mockk<port>(relaxed = true)

    val service = usecase(repository)
    test("Book should be added") {
        val book = Book("1984", "George Orwell")

        service.addBook(book)

        verify { repository.save(book) }
    }

    test ("Author should not be empty") {

        shouldThrowWithMessage<IllegalArgumentException>("Author should not be empty") {
            service.addBook(Book(author = "", title = "dummy"))
        }
    }

    test ("Title should not be empty") {
        shouldThrowWithMessage< IllegalArgumentException>("Title should not be empty") {
            service.addBook(Book(author = "dummy", title = ""))
        }
    }

    test ("List should be sorted") {
        val books = listOf(
            Book("Zebra", "Author Z"),
            Book("Apple", "Author A"),
            Book("Monkey", "Author M")
        )

        every { repository.findAll() } returns books

        val result = service.getAllBooks()

        result shouldBe listOf(
            Book("Apple", "Author A"),
            Book("Monkey", "Author M"),
            Book("Zebra", "Author Z")
        )
    }

    test ("List should contain all book") {
        val storedBooks = listOf(
            Book("Zebra", "Author Z"),
            Book("Apple", "Author A"),
            Book("Monkey", "Author M")
        )

        every { repository.findAll() } returns storedBooks

        val result = service.getAllBooks()

        result shouldContainExactlyInAnyOrder storedBooks
    }

})