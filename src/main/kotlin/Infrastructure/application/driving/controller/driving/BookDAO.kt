package Infrastructure.application.driving.controller.driving

import com.example.demo.domain.Book
import com.example.demo.domain.port
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO ( val namedParameterJdbcTemplate: NamedParameterJdbcTemplate) : port {

    private val rowMapper = RowMapper { rs, _ ->
        Book(
            title = rs.getString("title"),
            author = rs.getString("author") ,
            reserved = rs.getBoolean("reserved")
        )
    }

    override fun save(book: Book) {
        val sql = """
    INSERT INTO books (title, author, reserved)
    VALUES (:title, :author, :reserved)
""".trimIndent()

        val params = MapSqlParameterSource()
            .addValue("title", book.title)
            .addValue("author", book.author)
            .addValue("reserved", book.reserved)

        namedParameterJdbcTemplate.update(sql, params)

        val count = namedParameterJdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM books WHERE title = :title",
            MapSqlParameterSource("title", "Test"),
            Int::class.java
        )
        println("Nombre de livres en base avec ce titre : $count")
    }

    override fun findAll(): List<Book> {
        return namedParameterJdbcTemplate.query("SELECT * FROM books", rowMapper)
    }

    override fun reserve(title: String) {
        val sql = "UPDATE books SET reserved = true WHERE title = :title"
        val params = MapSqlParameterSource("title", title)
        namedParameterJdbcTemplate.update(sql, params)
    }

     override fun findByTitle(title: String): Book? {
        val sql = "SELECT * FROM books WHERE title = :title"
        val params = MapSqlParameterSource("title", title)
        return namedParameterJdbcTemplate.query(sql, params){ rs, _ ->
            Book(
                title = rs.getString("title"),
                author = rs.getString("author"),
                reserved = rs.getBoolean("reserved")
            )
        }.firstOrNull()
    }

}