package com.example.book.management.presentation.controller

import com.example.book.management.domain.model.Book
import com.example.book.management.domain.model.BookRequest
import com.example.book.management.domain.repository.BookRepository
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 書籍コントローラ
 */
@RestController
@RequestMapping("books")
@CrossOrigin
class BookController(private val bookRepository: BookRepository) {

    @GetMapping
    fun getBooks(): List<Book> = bookRepository.findAll()

    @GetMapping("{id}")
    fun findBookById(@PathVariable(value = "id") bookId: Long): ResponseEntity<Book> =
        bookRepository.findById(bookId).map { book ->
            ResponseEntity.ok(book)
        }.orElse(ResponseEntity.notFound().build())

    @PostMapping
    fun createNewBook(@RequestBody @Validated request: BookRequest): Book =
        bookRepository.save(Book(null, request.title, request.author))

    @PostMapping("{id}")
    fun updateBookById(
        @PathVariable(value = "id") bookId: Long,
        @RequestBody @Validated newBook: BookRequest
    ): ResponseEntity<Book> =
        bookRepository.findById(bookId).map { existingBook ->
            val updateBook = existingBook.copy(title = newBook.title, author = newBook.author)
            ResponseEntity.ok().body(bookRepository.save(updateBook))
        }.orElse(ResponseEntity.notFound().build())

    @GetMapping(params = ["title"])
    fun findByTitle(@RequestParam("title") title: String): List<Book> =
        bookRepository.findByTitleContaining(title)

    @GetMapping(params = ["author"])
    fun findByAuthor(@RequestParam("author") author: String): List<Book> =
        bookRepository.findByAuthorContaining(author)

}
