package com.example.book.management.domain.model

import javax.validation.constraints.NotBlank

/**
 * 書籍リクエスト
 */
data class BookRequest (

    @field:NotBlank
    val title: String,

    @field:NotBlank
    val author: String
)