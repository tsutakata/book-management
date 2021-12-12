package com.example.book.management.domain.model

import javax.validation.constraints.NotBlank

/**
 * 書籍リクエスト
 */
data class BookRequest (

    /** 書名 */
    @field:NotBlank
    val title: String,

    /** 著者名 */
    @field:NotBlank
    val author: String
)