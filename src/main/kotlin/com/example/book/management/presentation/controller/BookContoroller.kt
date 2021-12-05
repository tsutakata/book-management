package com.example.book.management.presentation.controller

import com.example.book.management.application.service.BookService
import com.example.book.management.domain.model.Book
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

/**
 * 書籍コントローラ
 */
@RestController
@RequestMapping("books")
@CrossOrigin
class BookController(private val bookService: BookService){

    @GetMapping
    fun getBooks(): List<Book> = bookService.findAll()

    @GetMapping("/{id}")
    fun findBookById(@PathVariable(value = "id") bookId: Long): ResponseEntity<Book>{
        val book: Book = bookService.findById(bookId)
        return ResponseEntity.ok(book)
    }

    @PostMapping
    fun createNewBook(@RequestBody book: Book): Book = bookService.save(book)

    @PostMapping("/{id}")
    fun updateBookById(@PathVariable(value = "id") bookId: Long,
                       @RequestBody newBook: Book): ResponseEntity<Book>{
        val existingBook = bookService.findById(bookId)
        return run {
            val updateBook: Book = existingBook.copy(title = newBook.title)
            bookService.save(updateBook)
            ResponseEntity.ok(updateBook)
        }
    }

    @GetMapping(params = ["title"])
    fun findByTitle(@RequestParam("title") title: String): List<Book> = bookService.findByTitle(title)

    @GetMapping(params = ["author"])
    fun findByAuthor(@RequestParam("author") author: String): List<Book> = bookService.findByAuthor(author)

    @GetMapping(params = ["author", "field"])
    fun findTitleByAuthor(@RequestParam("author") author: String,
                          @RequestParam("field") field: String): MutableList<String?>? {
        val books: List<Book> =  bookService.findByAuthor(author)
        return if("title" == field){
            books.stream().map { book -> book.title }.collect(Collectors.toList())
        }else{
            books.stream().map { book -> book.author }.collect(Collectors.toList())
        }
    }

}
