package com.example.book.management.domain.repository

import com.example.book.management.domain.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 書籍リポジトリ
 */
@Repository
interface BookRepository : JpaRepository<Book, Long>{

    /**
     * 書名を指定して、書籍情報を検索する
     */
    fun findByTitleContaining(title: String) : List<Book>

    /**
     * 著者名を指定して、書籍情報を検索する
     */
    fun findByAuthorContaining(author: String) : List<Book>
}