package com.example.book.management.domain.repository

import com.example.book.management.domain.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 書籍リポジトリ
 */
@Repository
interface BookRepository : JpaRepository<Book, Long>{

    fun findByTitleContaining(title: String) : List<Book>
    fun findByAuthorContaining(author: String) : List<Book>
}