package com.example.book.management.domain.repository

import com.example.book.management.domain.model.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * 書籍リポジトリ
 */
@Repository
interface BookRepository : JpaRepository<Book, Long> {

    /**
     * 著者名を指定して、書籍情報を取得する（完全一致）
     */
    fun findByAuthor(author: String): List<Book>

    /**
     * 書名を指定して、書籍情報を取得する（部分一致）
     */
    fun findByTitleContaining(title: String): List<Book>

    /**
     * 著者名を指定して、書籍情報を検索する（部分一致）
     */
    fun findByAuthorContaining(author: String): List<Book>
}