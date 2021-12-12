package com.example.book.management.domain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * 書籍エンティティ
 */
@Entity
data class Book(

    /** 書籍ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    /** 書名 */
    val title: String,

    /** 著者名 */
    val author: String
)