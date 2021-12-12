package com.example.book.management.presentation.controller

import com.example.book.management.domain.model.Book
import com.example.book.management.domain.model.BookRequest
import com.example.book.management.domain.repository.BookRepository
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

/**
 * 書籍コントローラ
 */
@RestController
@RequestMapping("books")
@CrossOrigin
class BookController(private val bookRepository: BookRepository) {

    /**
     * すべての書籍情報を取得する
     */
    @GetMapping
    fun findAllBooks(): List<Book> = bookRepository.findAll()

    /**
     * 指定された書籍IDの書籍情報を取得する
     */
    @GetMapping("{id}")
    fun findBookById(@PathVariable(value = "id") bookId: Long): ResponseEntity<Book> =
        bookRepository.findById(bookId).map { book ->
            ResponseEntity.ok(book)
        }.orElse(ResponseEntity.notFound().build())

    /**
     * 書籍情報を新規登録する
     */
    @PostMapping
    fun createNewBook(@RequestBody @Validated request: BookRequest): Book =
        bookRepository.save(Book(null, request.title, request.author))

    /**
     * 書籍情報を更新する
     */
    @PostMapping("{id}")
    fun updateBookById(
        @PathVariable(value = "id") bookId: Long,
        @RequestBody @Validated newBook: BookRequest
    ): ResponseEntity<Book> =
        bookRepository.findById(bookId).map { existingBook ->
            val updateBook = existingBook.copy(title = newBook.title, author = newBook.author)
            ResponseEntity.ok().body(bookRepository.save(updateBook))
        }.orElse(ResponseEntity.notFound().build())

    /**
     * 書名を指定して、書籍情報を検索する
     */
    @GetMapping(params = ["title"])
    fun findBooksByTitle(@RequestParam("title") title: String): List<Book> =
        bookRepository.findByTitleContaining(title)

    /**
     * 著者名を指定して、書籍情報を検索する
     */
    @GetMapping(params = ["author"])
    fun findBooksByAuthor(@RequestParam("author") author: String): List<Book> =
        bookRepository.findByAuthorContaining(author)

    /**
     * 著者名を指定して、該当する著者の書名リストを取得する
     */
    @GetMapping(params = ["author", "field"])
    fun findBookTitlesByAuthor(
        @RequestParam("author") author: String,
        @RequestParam("field") field: String): ResponseEntity<List<String>> {

        if(field != "title"){
            return ResponseEntity.badRequest().build()
        }
        val books: List<Book> =  bookRepository.findByAuthor(author)
        val titles: List<String> = books.stream().map { book -> book.title }.collect(Collectors.toList())
        return ResponseEntity.ok().body(titles)
    }

}
