package com.example.demo


import Infrastructure.application.driving.controller.driving.BookDAO
import com.example.demo.domain.Book
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.containers.PostgreSQLContainer
import org.springframework.beans.factory.annotation.Autowired
import org.junit.jupiter.api.TestInstance
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

@SpringBootTest
@ActiveProfiles("testIntegration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDAOIT @Autowired constructor(
    private val dao: BookDAO
) : FunSpec({

    test("should save and retrieve a book") {
        dao.saveBook(Book("Test", "Author"))
        val found = dao.findByTitle("Test")
        found shouldNotBe null
        found!!.title shouldBe "Test"
        found.author shouldBe "Author"
        found.reserved shouldBe false
    }

    test("should update reserved status") {
        dao.saveBook(Book("ToReserve", "Author"))
        dao.reserve("ToReserve")
        val reservedBook = dao.findByTitle("ToReserve")
        reservedBook!!.reserved shouldBe true
    }

    afterEach {
        val templateField = dao::class.java.getDeclaredField("jdbcTemplate")
        templateField.isAccessible = true
        val jdbcTemplate = templateField.get(dao) as NamedParameterJdbcTemplate
        jdbcTemplate.jdbcTemplate.execute("DELETE FROM books")
    }

}) {
    companion object {
        private val container = PostgreSQLContainer("postgres:13-alpine").apply {
            withDatabaseName("booktest")
            withUsername("user")
            withPassword("password")
            start()
        }

        init {
            System.setProperty("spring.datasource.url", container.jdbcUrl)
            System.setProperty("spring.datasource.username", container.username)
            System.setProperty("spring.datasource.password", container.password)
            System.setProperty("spring.datasource.driver-class-name", container.driverClassName)
        }
    }
}