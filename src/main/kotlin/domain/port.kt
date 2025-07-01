package domain

interface port {
    fun save(book: Book)
    fun findAll(): List<Book>
}