package Infrastructure.application.driving.controller

import Infrastructure.application.driving.controller.DTO.BookDTO
import com.example.demo.domain.usecase
import com.example.demo.domain.Book
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/books")
class BookController (
    val service: usecase
){

    @GetMapping
    fun getAll(): List<BookDTO> =
        service.getAllBooks().map { BookDTO(it.title, it.author, it.reserved) }

    @PostMapping
    fun addBook(@RequestBody dto: BookDTO) {
        service.addBook(Book(dto.title, dto.author))
    }

    @PostMapping("/reserve")
    fun reserveBook(@RequestBody dto: BookDTO): ResponseEntity<Any> {
        return try {
            service.reserveBook(dto.title)
            ResponseEntity.ok().build()
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.CONFLICT).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur")
        }
    }
}
