package com.example.book.management.presentation.controller

import com.example.book.management.application.service.BookService
import com.example.book.management.domain.model.Book
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

/**
 * 書籍コントローラ
 */
@RestController
@RequestMapping("books")
@CrossOrigin
@Validated
class BookController(private val bookService: BookService) {

    @GetMapping
    fun getBooks(): List<Book> = bookService.findAll()

    @GetMapping("/{id}")
    fun findBookById(@PathVariable(value = "id") bookId: Long) =
        bookService.findById(bookId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This book does not exist")

    @PostMapping
    fun createNewBook(@RequestBody book: Book): Book = bookService.save(book)

    @PostMapping("/{id}")
    fun updateBookById(
        @PathVariable(value = "id") bookId: Long,
        @RequestBody newBook: Book
    ): ResponseEntity<Book> {
        val existingBook = bookService.findById(bookId)
        return if (existingBook != null) {
            val updateBook: Book = existingBook.copy(title = newBook.title)
            bookService.save(updateBook)
            ResponseEntity.ok(updateBook)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(params = ["title"])
    fun findByTitle(@RequestParam("title") title: String): List<Book> = bookService.findByTitle(title)

    @GetMapping(params = ["author"])
    fun findByAuthor(@RequestParam("author") author: String): List<Book> = bookService.findByAuthor(author)

}
