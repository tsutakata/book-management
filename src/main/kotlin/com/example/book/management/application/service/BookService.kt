package com.example.book.management.application.service

import com.example.book.management.domain.repository.BookRepository
import com.example.book.management.domain.model.Book
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * 書籍サービス
 */
@Service
class BookService(
    @Autowired
    private val bookRepository: BookRepository
) {
    fun findAll() = bookRepository.findAll()
    fun findById(id: Long) = bookRepository.findById(id).orElse(null)
    fun save(book: Book) = bookRepository.save(book)
    fun findByTitle(title: String): List<Book> = bookRepository.findByTitleContaining(title)
    fun findByAuthor(author: String): List<Book> = bookRepository.findByAuthorContaining(author)
}