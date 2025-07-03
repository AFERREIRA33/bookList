package Infrastructure.application.driving.controller.driving

import com.example.demo.domain.Book
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO ( val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) {

    private val rowMapper = RowMapper { rs, _ ->
        Book(
            title = rs.getString("title"),
            author = rs.getString("author")
        )
    }

    fun getAllBooks(): List<Book> {
        return namedParameterJdbcTemplate.query("SELECT * FROM books", rowMapper)
    }
    fun saveBook(book: Book) {
        val sql = """
            INSERT INTO books (title, author)
            VALUES (:title, :author)
        """.trimIndent()

        val params = MapSqlParameterSource()
            .addValue("title", book.title)
            .addValue("author", book.author)
        namedParameterJdbcTemplate.update(sql, params)
    }

    fun reserve(title: String) {
        val sql = "UPDATE books SET reserved = true WHERE title = :title"
        val params = MapSqlParameterSource("title", title)
        namedParameterJdbcTemplate.update(sql, params)
    }

     fun findByTitle(title: String): Book? {
        val sql = "SELECT * FROM books WHERE title = :title"
        val params = MapSqlParameterSource("title", title)
        return namedParameterJdbcTemplate.query(sql, params, rowMapper).firstOrNull()
    }

}